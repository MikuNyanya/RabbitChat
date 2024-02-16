package cn.mikulink.rabbitchat.entity.response;


import cn.mikulink.rabbitchat.content.RequestContent;

/**
 * MikuLink created in 2024/2/16 3:17
 * For the Reisen
 * <p>
 * 返回给前端时所使用的构建类
 * 选择合适的构建方法，以便减少在controller的代码量
 */
public class ResponseBean {

    //请求成功
    public static ResponseInfo success() {
        return create(RequestContent.CODE_SUCESS, RequestContent.CODE_SUCESS_TEXT, null, null);
    }

    //请求成功 附带数据
    public static ResponseInfo success(Object data) {
        return create(RequestContent.CODE_SUCESS, RequestContent.CODE_SUCESS_TEXT, null, data);
    }

    //请求失败
    public static ResponseInfo error() {
        return create(RequestContent.CODE_ERROR, RequestContent.CODE_ERROR_TEXT, null, null);
    }

    //请求失败 附带信息
    public static ResponseInfo error(String errorMessage) {
        return create(RequestContent.CODE_ERROR, RequestContent.CODE_ERROR_TEXT, errorMessage, null);
    }

    //基本构建
    public static ResponseInfo create(int code, String message, String errrorMessage, Object data) {
        ResponseInfo rspInfo = new ResponseInfo();
        rspInfo.setCode(code);
        rspInfo.setMessage(message);
        rspInfo.setErrorMessage(errrorMessage);
        rspInfo.setData(data);
        return rspInfo;
    }
}
