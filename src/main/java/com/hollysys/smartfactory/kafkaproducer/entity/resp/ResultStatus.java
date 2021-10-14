package com.hollysys.smartfactory.kafkaproducer.entity.resp;

/**
 * @author lizhi186545
 */
public class ResultStatus {
    public static final int RESULT_STATUS_SUCCESS = 0;
    public static final int RESULT_STATUS_ERROR = -999;
    //请求错误
    public static final int RESULT_STATUS_400 = 400;
    //没有资源
    public static final int RESULT_STATUS_404 = 404;
    //服务器内部错误
    public static final int RESULT_STATUS_500 = 500;
    public static final String RESULT_MESSAGE_SUCCESS = "success";
    private String message;
    private Object data;
    private int status;

    public ResultStatus() {
    }

    public ResultStatus(String message) {
        this.message = message;
    }

    public ResultStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return this.data;
    }

    public ResultStatus setData(Object data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultStatus setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public ResultStatus setStatus(int status) {
        this.status = status;
        return this;
    }
}
