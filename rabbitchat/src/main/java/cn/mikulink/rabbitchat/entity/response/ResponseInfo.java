package cn.mikulink.rabbitchat.entity.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * MikuLink created in 2024/2/16 3:25
 * For the Reisen
 * <p>
 * 返回给前端的数据格式类
 */
public class ResponseInfo {

    /**
     * 请求状态码 原则上只用来标识请求状态 而非业务状态
     * 应与常规http请求状态对应
     * 200 成功
     * 500 失败
     */
    @Getter
    @Setter
    private int code;

    /**
     * 业务状态码 原则上只用于标识业务处理状态
     * 如有业务需要，可独立定义状态码与前端对应
     * 0 成功
     * 1 失败
     */
    @Getter
    @Setter
    private int status;

    /**
     * 返回信息
     * 原则上只用于正常信息的传递
     * 比如 "创建成功" "删除失败"
     */
    @Getter
    @Setter
    private String message;

    /**
     * 失败信息
     * 原则上只用于错误信息传递
     * 比如 "名称不能为空" "只能为纯数字"
     */
    @Getter
    @Setter
    private String errorMessage;

    /**
     * 返回数据
     */
    @Getter
    @Setter
    private Object data;

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}

