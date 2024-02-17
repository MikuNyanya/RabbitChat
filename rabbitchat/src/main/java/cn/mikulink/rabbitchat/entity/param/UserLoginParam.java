package cn.mikulink.rabbitchat.entity.param;

import lombok.Getter;
import lombok.Setter;

/**
 * MikuLink created in 2024/2/18 2:29
 * For the Reisen
 * <p>
 * 用户登录参数
 */
@Getter
@Setter
public class UserLoginParam {
    /**
     * 登录账号
     */
    private String account;
    /**
     * 登录密码 该参数应在传递到服务器前就是密文
     */
    private String password;
    /**
     * 是否记住用户登录 该参数会影响服务器上用户登录状态的时效
     * 0.否 1.是
     */
    private Integer rememberMe = 0;
}
