package com.hollysys.smartfactory.kafkaproducer.controller;


import com.hollysys.smartfactory.kafkaproducer.entity.resp.ResultStatus;

/**
 * @author lizhi186545
 */
public class BaseController {
    public static final String HEADER_UNKNOWN = "unknown";

    public BaseController() {
    }

    public ResultStatus error(String message) {
        return (new ResultStatus(-999)).setMessage(message);
    }

    public ResultStatus error(String message, int status) {
        return (new ResultStatus(-999)).setMessage(message).setStatus(status);
    }

    public ResultStatus success(String message) {
        return (new ResultStatus(0)).setMessage(message);
    }

    public ResultStatus success(String message, Object results) {
        return (new ResultStatus(0)).setMessage(message).setData(results);
    }

}
