package com.mars.pay.model;

import com.mars.pay.enums.RequestType;

/**
 * @author hufeng
 * @version ApiConfig.java, v 0.1 2020/3/21 12:16 AM Exp $
 */

public class ApiConfig {
    /**
     * api标示
     */
    private String  id;
    /**
     * API
     */
    private String  api;
    /**
     * 请求方法 GET or POST
     *
     * @see RequestType
     */
    private String  requestType;
    /**
     * 请求参数是否包含加密字段
     */
    private boolean encrypted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
