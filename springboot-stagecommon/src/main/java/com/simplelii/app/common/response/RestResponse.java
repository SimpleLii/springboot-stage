package com.simplelii.app.common.response;

/**
 * @author SimpleLii
 * @description api返回包装对象
 * @date 16:24 2020/6/18
 */
public class RestResponse<T> {

    private static final String SUCCESS_CODE = "0";
    private static final String SUCCESS_MSG = "SUCCESS";

    // 请求返回码
    private String resultCode;
    // 请求返回信息
    private String resultMsg;
    // 请求返回对象
    private Object data;


    public static final RestResponse SUCCESS = new RestResponse(SUCCESS_CODE, SUCCESS_MSG, (Object) Void.class);


    public RestResponse() {
    }

    public RestResponse(Object data) {
        this.resultCode = SUCCESS_CODE;
        this.resultMsg = SUCCESS_MSG;
        this.data = data;
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
