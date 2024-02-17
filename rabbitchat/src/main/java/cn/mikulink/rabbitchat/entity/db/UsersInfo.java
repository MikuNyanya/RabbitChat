package cn.mikulink.rabbitchat.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * MikuLink created in 2024/2/16 4:59
 * For the Reisen
 * <p>
 * 用户信息
 */
@Getter
@Setter
@AllArgsConstructor //有参构造
@NoArgsConstructor  //无参构造
@Alias("users")
public class UsersInfo {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     * 记得加密传递
     */
    private String password;
    /**
     * 密码加盐
     */
    private String passwordSalt;
    /**
     * 用户头像
     */
    private String userImg;
    /**
     * 用户账号状态
     * -1.禁用 0.正常
     */
    private int userStatus;

}
