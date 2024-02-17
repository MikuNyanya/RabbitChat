package cn.mikulink.rabbitchat.entity.param;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * MikuLink created in 2024/2/18 2:29
 * For the Reisen
 * <p>
 * 用户登录参数
 */
@Getter
@Setter
@Alias("userRegisterParam")
public class UserRegisterParam {
    /**
     * 登录账号
     */
    private String account;
    /**
     * 登录密码 该参数应在传递到服务器前就是密文
     */
    private String password;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 用户头像
     */
    private String userImg;
}
