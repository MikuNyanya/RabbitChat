package cn.mikulink.rabbitchat.entity.response;

import lombok.Getter;
import lombok.Setter;

/**
 * MikuLink created in 2024/2/18 8:00
 * For the Reisen
 * <p>
 * 泛用方法返回信息对象
 */
@Getter
@Setter
public class MethodReInfo {

    public static MethodReInfo create() {
        return create(true, null);
    }

    public static MethodReInfo create(boolean isSucess, String message) {
        MethodReInfo info = new MethodReInfo();
        info.setSucess(isSucess);
        info.setMessage(message);
        return info;
    }

    /**
     * 是否成功
     */
    private boolean isSucess;
    /**
     * 返回信息
     */
    private String message;
//    /**
//     * 返回数据
//     */
//    private Object data;
}
