package com.mars.pay.transport.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hufeng
 * @version TransportRequest.java, v 0.1 2020/3/21 12:37 AM Exp $
 */

public class TransportRequest {
    /**
     * 业务标示
     */
    private String              id;
    /**
     * json参数
     */
    private String              jsonParams;
    /**
     * 请求参数中是否包含加密字段
     */
    private boolean             encrypted;
    /**
     * URL path variables
     */
    private Map<String, String> pathVariables = new HashMap<>();
    /**
     * 接口
     */
    private String              api;
    /**
     * 请求类型
     *
     * @see com.mars.pay.enums.RequestType
     */
    private String              requestType;
    /**
     * filename
     */
    private String              fileName;
    /**
     * file content
     */
    private byte[]              content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(String jsonParams) {
        this.jsonParams = jsonParams;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(Map<String, String> pathVariables) {
        this.pathVariables = pathVariables;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
