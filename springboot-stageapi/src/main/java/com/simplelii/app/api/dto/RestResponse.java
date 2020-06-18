package com.simplelii.app.api.dto;

/**
 * @author SimpleLii
 * @description api返回包装对象
 * @date 16:24 2020/6/18
 */
public class RestResponse<T> {

    private static final String SUCCESS_CODE = "0";
    private static final String SUCCESS_MSG = "请求成功";

    // 请求返回码
    private String resultCode;
    // 请求返回信息
    private String resultMsg;
    // 请求返回对象
    private Object data;


    public static final RestResponse SUCCESS = new RestResponse(SUCCESS_CODE, SUCCESS_MSG, (Void) new Object());


    public RestResponse() {
    }

    public RestResponse(String resultCode, String resultMsg, Object data) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
