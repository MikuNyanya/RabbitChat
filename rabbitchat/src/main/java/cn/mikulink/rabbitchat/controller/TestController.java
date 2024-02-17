package cn.mikulink.rabbitchat.controller;

import cn.mikulink.rabbitchat.redis.RabbitRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MikuLink created in 2024/2/16 2:42
 * For the Reisen
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    private RabbitRedisService rabbitRedisService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "test success";
    }

    @RequestMapping(value = "/redisset", method = RequestMethod.GET)
    @ResponseBody
    public String redis(String value) {
        rabbitRedisService.set("test", value);
        return "success";
    }

    @RequestMapping(value = "/redisget", method = RequestMethod.GET)
    @ResponseBody
    public String redis() {
        return rabbitRedisService.get("test");
    }

}
