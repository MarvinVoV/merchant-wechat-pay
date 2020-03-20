package com.mars.pay.model;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hufeng
 * @version InnerRequest.java, v 0.1 2020/3/20 11:49 PM Exp $
 */

public class InnerRequest {
    /**
     * HTTP Header
     */
    protected Map<String, String> headers;
    /**
     * URL
     */
    protected String              url;
    /**
     * 扩展信息
     */
    protected Map<String, String> extInfo;

    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!StringUtils.isBlank(key)) {
            headers.put(key, value);
        }
    }

    public void putExtInfo(String key, String value) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("key不能为空");
        }
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }
        extInfo.put(key, value);
    }

    public String fetchExtInfo(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return extInfo.get(key);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }
}
