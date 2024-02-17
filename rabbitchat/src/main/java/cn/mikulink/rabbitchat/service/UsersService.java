package cn.mikulink.rabbitchat.service;

import cn.mikulink.rabbitchat.content.RedisContent;
import cn.mikulink.rabbitchat.entity.db.UsersInfo;
import cn.mikulink.rabbitchat.entity.param.UserLoginParam;
import cn.mikulink.rabbitchat.entity.param.UserRegisterParam;
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
        usersInfo.setAccount(param.getAccount());
        usersInfo.setName(param.getName());
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


    public String login(UserLoginParam param) throws RabbitException {
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
            if (!saltPwd.equals(param.getPassword())) {
                throw new RabbitException("密码错误");
            }
        } catch (NoSuchAlgorithmException nex) {
            log.error("用户创建时密码加密异常", nex);
            throw new RabbitException("密码加密异常");
        }

        //身份验证完成
        //todo 刷新最后登录时间


        //生成会话权限码
        String chatAuth = createChatAuth(usersInfo);
        //权限码写入redis 默认24小时
        String key = appName + "_" + RedisContent.KEY_CHATAUTH + usersInfo.getId();
        rabbitRedisService.set(key, chatAuth, 24L, TimeUnit.HOURS);

        return chatAuth;
    }

    private String createChatAuth(UsersInfo usersInfo) {
        //授权码格式 兔子,uid,创建时间
        String authStr = String.format("%s,%s,%s", appName, usersInfo.getId(), DateUtil.toString(new Date()));
        String chatAuth = Des3Util.encrypt(des3Key, authStr);
        log.info("生成会话权限,{},{}", usersInfo.getId(), chatAuth);
        return chatAuth;
    }
}
