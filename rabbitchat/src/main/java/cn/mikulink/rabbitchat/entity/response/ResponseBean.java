package cn.mikulink.rabbitchat.entity.response;


import cn.mikulink.rabbitchat.content.RequestContent;
import com.alibaba.fastjson.JSONObject;

/**
 * MikuLink created in 2024/2/16 3:17
 * For the Reisen
 * <p>
 * 返回给前端时所使用的构建类
 * 选择合适的构建方法，以便减少在controller的代码量
 */
public class ResponseBean {

    //业务处理成功
    public static String bizSuccess() {
        return JSONObject.toJSONString(bizSuccess(RequestContent.STATUS_SUCESS_TEXT));
    }

    //业务处理成功
    public static String bizSuccess(String msg) {
        return bizSuccess(msg, null);
    }

    //业务处理成功
    public static String bizSuccess(String msg, Object data) {
        return JSONObject.toJSONString(create(RequestContent.CODE_SUCESS, RequestContent.STATUS_SUCCESS, msg, null, data));
    }

    //业务处理失败
    public static String bizFail() {
        return bizFail(RequestContent.STATUS_FAIL_TEXT);
    }

    //业务处理失败
    public static String bizFail(String errMsg) {
        return bizFail(RequestContent.STATUS_FAIL_TEXT, errMsg);
    }

    //业务处理失败
    public static String bizFail(String msg, String errMsg) {
        return bizFail(msg, errMsg, null);
    }

    //业务处理失败
    public static String bizFail(String msg, String errMsg, Object data) {
        return JSONObject.toJSONString(create(RequestContent.CODE_SUCESS, RequestContent.STATUS_FAIL, msg, errMsg, data));
    }


    //请求成功
    public static String success() {
        return success(null);
    }

    //请求成功 附带数据
    public static String success(Object data) {
        return JSONObject.toJSONString(create(RequestContent.CODE_SUCESS, null, RequestContent.CODE_SUCESS_TEXT, null, data));
    }

    //请求失败
    public static String error() {
        return error(RequestContent.CODE_ERROR_TEXT);
    }

    //请求失败 附带信息
    public static String error(String errorMessage) {
        return JSONObject.toJSONString(create(RequestContent.CODE_ERROR, null, RequestContent.CODE_ERROR_TEXT, errorMessage, null));
    }

    //基本构建
    public static ResponseInfo create(int code, Integer status, String message, String errrorMessage, Object data) {
        ResponseInfo rspInfo = new ResponseInfo();
        rspInfo.setCode(code);
        if (null != status) {
            rspInfo.setStatus(status);
        }
        rspInfo.setMessage(message);
        rspInfo.setErrorMessage(errrorMessage);
        rspInfo.setData(data);
        return rspInfo;
    }
}
