package cn.mikulink.rabbitchat.controller;

import cn.mikulink.rabbitchat.entity.response.ResponseBean;
import cn.mikulink.rabbitchat.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * MikuLink created in 2024/2/16 2:42
 * For the Reisen
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return ResponseBean.success(usersService.getAll()).toJsonString();
    }

}
