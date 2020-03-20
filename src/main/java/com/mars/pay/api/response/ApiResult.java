package com.mars.pay.api.response;

/**
 * @author hufeng
 * @version ApiResult.java, v 0.1 2020/3/21 12:23 AM Exp $
 */

public class ApiResult<T> {
    private boolean success;
    private String  code;
    private String  message;
    private T       data;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
