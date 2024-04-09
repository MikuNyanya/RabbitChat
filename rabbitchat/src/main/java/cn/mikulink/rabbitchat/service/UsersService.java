package cn.mikulink.rabbitchat.service;

import cn.mikulink.rabbitchat.content.RedisContent;
import cn.mikulink.rabbitchat.entity.db.UsersInfo;
import cn.mikulink.rabbitchat.entity.param.UserLoginParam;
import cn.mikulink.rabbitchat.entity.param.UserRegisterParam;
import cn.mikulink.rabbitchat.entity.response.MethodReInfo;
import cn.mikulink.rabbitchat.entity.response.user.UserLoginVo;
import cn.mikulink.rabbitchat.exceptions.RabbitException;
import cn.mikulink.rabbitchat.mapper.UsersMapper;
import cn.mikulink.rabbitchat.redis.RabbitRedisService;
import cn.mikulink.rabbitchat.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * MikuLink created in 2024/2/16 5:18
 * For the Reisen
 * <p>
 * 用户信息服务
 */
@Service
@Slf4j
public class UsersService {
    @Value("${rabbitchat.des3key}")
    private String des3Key;
    @Value("${rabbitchat.nameen}")
    private String appName;

    @Autowired
    private UsersMapper mapper;
    @Autowired
    private RabbitRedisService rabbitRedisService;

    /**
     * 用户注册
     *
     * @param param 注册参数
     */
    public void register(UserRegisterParam param) throws RabbitException {
        //基本参数检查
        if (null == param) {
            throw new RabbitException("注册信息有误");
        }
        if (StringUtil.isEmpty(param.getAccount())) {
            throw new RabbitException("账号不能为空");
        }
        if (param.getAccount().length() > 50) {
            throw new RabbitException("账号最长50个字符");
        }
        if (StringUtil.isEmpty(param.getPassword())) {
            throw new RabbitException("密码不能为空");
        }

        //检测账号唯一性
        Integer count = mapper.existsAccount(param.getAccount());
        if (null != count && count > 0) {
            throw new RabbitException("账号已存在");
        }

        //转化为用户信息
        UsersInfo usersInfo = new UsersInfo();
        usersInfo.setCreateTime(new Date());
        usersInfo.setAccount(param.getAccount());
        usersInfo.setName(param.getName());
        //如果没有填写用户名 默认使用账号做为名称
        if (StringUtil.isEmpty(usersInfo.getName())) {
            usersInfo.setName(usersInfo.getAccount());
        }
        //如果没设置头像，使用默认头像
        if (StringUtil.isEmpty(param.getUserImg())) {
            usersInfo.setUserImg("");
        }
        usersInfo.setUserStatus(0);

        //密码加盐
        try {
            String salt = String.valueOf(RandomUtil.roll(99999));
            String password = MD5Util.md5(param.getPassword(), salt);
            usersInfo.setPassword(password);
            usersInfo.setPasswordSalt(salt);
        } catch (NoSuchAlgorithmException nex) {
            log.error("用户创建时密码加密异常", nex);
            throw new RabbitException("密码加密异常");
        }

        mapper.create(usersInfo);
    }

    /**
     * 用户登录
     *
     * @param param 登录信息
     * @return 登录后给于sid 会话权限码等
     * @throws RabbitException 业务异常
     */
    public UserLoginVo login(UserLoginParam param) throws RabbitException {
        //基本参数检查
        if (null == param) {
            throw new RabbitException("登录信息有误");
        }
        if (StringUtil.isEmpty(param.getAccount())) {
            throw new RabbitException("账号信息有误");
        }
        UsersInfo usersInfo = mapper.getOne(param.getAccount());
        if (null == usersInfo) {
            throw new RabbitException("账号不存在");
        }

        //对比密码
        if (StringUtil.isEmpty(param.getPassword())) {
            throw new RabbitException("密码错误");
        }
        try {
            String salt = usersInfo.getPasswordSalt();
            String saltPwd = MD5Util.md5(param.getPassword(), salt);
            if (!saltPwd.equals(usersInfo.getPassword())) {
                throw new RabbitException("密码错误");
            }
        } catch (NoSuchAlgorithmException nex) {
            log.error("用户创建时密码加密异常", nex);
            throw new RabbitException("密码加密异常");
        }

        //生成会话权限码
        String chatAuth = createChatAuth(usersInfo);
        //权限码写入redis 默认24小时
        String key = appName + "_" + RedisContent.KEY_CHATAUTH + usersInfo.getId();
        rabbitRedisService.set(key, chatAuth, 24L, TimeUnit.HOURS);

        UserLoginVo loginVo = new UserLoginVo();
        loginVo.setUid(usersInfo.getId());
        loginVo.setChatAuth(chatAuth);

        return loginVo;
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UsersInfo getById(Long userId) {
        //todo 访问量一定大 走缓存减少db访问量
        return this.mapper.getById(userId);
    }


    /**
     * 根据用户信息创建会话权限码
     *
     * @param usersInfo 用户信息
     * @return 会话权限码
     */
    private String createChatAuth(UsersInfo usersInfo) {
        //授权码格式 兔子,uid,创建时间
        String authStr = String.format("%s,%s,%s", appName, usersInfo.getId(), DateUtil.toString(new Date()));
        String chatAuth = Des3Util.encrypt(des3Key, authStr);
        log.info("生成会话权限:{},{}", usersInfo.getId(), chatAuth);
        return chatAuth;
    }

    /**
     * 检查会话权限码是否有效
     *
     * @param sid      websock的sid
     * @param chatAuth 会话权限码
     * @return 检查结果
     */
    public MethodReInfo chatAuthCheck(String sid, String chatAuth) {
        if (null == sid) {
            return MethodReInfo.create(false, "sid为空");
        }
        if (!NumberUtil.isNumberOnly(sid)) {
            return MethodReInfo.create(false, "sid类型错误");
        }

        //检查会话授权格式
        if (StringUtil.isEmpty(chatAuth)) {
            return MethodReInfo.create(false, "会话权限无效");
        }
        try {
            String chatAuthStr = Des3Util.decrypt(des3Key, chatAuth);
            String[] chatAuthParams = chatAuthStr.split(",");
            if (chatAuthParams.length != 3
                    || !chatAuthParams[0].equals(appName)
                    || !chatAuthParams[1].equals(sid)) {
                return MethodReInfo.create(false, "会话权限无效");
            }
        } catch (Exception ex) {
            log.error("会话权限验证解密异常", ex);
            return MethodReInfo.create(false, "会话权限错误");
        }

        UsersInfo usersInfo = mapper.getById(NumberUtil.toLong(sid));
        if (null == usersInfo || null == usersInfo.getId()) {
            return MethodReInfo.create(false, "sid不存在");
        }

        return MethodReInfo.create();
    }
}
