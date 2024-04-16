package cn.mikulink.rabbitchat.controller;

import cn.mikulink.rabbitchat.entity.param.UserLoginParam;
import cn.mikulink.rabbitchat.entity.param.UserRegisterParam;
import cn.mikulink.rabbitchat.entity.response.ResponseBean;
import cn.mikulink.rabbitchat.entity.response.user.UserLoginVo;
import cn.mikulink.rabbitchat.exceptions.RabbitException;
import cn.mikulink.rabbitchat.service.UsersService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * MikuLink created in 2024/2/18 2:22
 * For the Reisen
 * <p>
 * 用户
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UsersService usersService;

    /**
     * 用户登录
     *
     * @param param 账号密码
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody UserLoginParam param) {
        try {
            UserLoginVo loginVo = usersService.login(param);
            return ResponseBean.bizSuccess("登录成功", JSONObject.toJSONString(loginVo));
        } catch (RabbitException rabEx) {
            log.debug("用户登录失败", rabEx);
            return ResponseBean.bizFail(rabEx.getMessage());
        } catch (Exception ex) {
            log.error("用户登录异常", ex);
            return ResponseBean.error();
        }
    }

    /**
     * 用户注册
     *
     * @param param 注册信息
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody UserRegisterParam param) {
        try {
            usersService.register(param);
            return ResponseBean.bizSuccess("注册成功");
        } catch (RabbitException rabEx) {
            log.debug("用户注册失败", rabEx);
            return ResponseBean.bizFail(rabEx.getMessage());
        } catch (Exception ex) {
            log.error("用户注册异常", ex);
            return ResponseBean.error();
        }
    }

    @RequestMapping(value = "logoList", method = RequestMethod.GET)
    @ResponseBody
    public String logoList() {
        try {
            List<String> logos = usersService.getLogoList();
            return ResponseBean.bizSuccess("获取成功", JSONObject.toJSONString(logos));
        } catch (Exception ex) {
            log.error("获取用户头像列表异常", ex);
            return ResponseBean.error();
        }
    }
}
