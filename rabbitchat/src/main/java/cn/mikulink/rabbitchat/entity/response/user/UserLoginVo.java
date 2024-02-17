package cn.mikulink.rabbitchat.entity.response.user;

import lombok.Getter;
import lombok.Setter;

/**
 * MikuLink created in 2024/2/18 4:45
 * For the Reisen
 */
@Getter
@Setter
public class UserLoginVo {
    //会话权限码
    private String chatAuth;
}
