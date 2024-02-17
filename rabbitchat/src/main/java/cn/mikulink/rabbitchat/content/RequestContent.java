package cn.mikulink.rabbitchat.content;

/**
 * MikuLink created in 2024/2/16 3:40
 * For the Reisen
 *
 * 存储请求相关的常量
 */
public class RequestContent extends CommonCentent{
    //请求成功
    public static final int CODE_SUCESS = 200;
    public static final String CODE_SUCESS_TEXT = "请求成功";

    //请求异常
    public static final int CODE_ERROR = 500;
    public static final String CODE_ERROR_TEXT = "请求错误";

    //业务状态
    public static final int STATUS_SUCCESS = 0;
    public static final String STATUS_SUCESS_TEXT = "处理成功";
    public static final int STATUS_FAIL = 1;
    public static final String STATUS_FAIL_TEXT = "处理失败";
}
