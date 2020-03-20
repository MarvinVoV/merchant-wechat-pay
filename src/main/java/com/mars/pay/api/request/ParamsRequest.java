package com.mars.pay.api.request;

import java.util.Map;

/**
 * @author hufeng
 * @version ParamsRequest.java, v 0.1 2020/3/21 12:22 AM Exp $
 */

public class ParamsRequest extends Request{
    /**
     * api 标示
     */
    private String              id;
    /**
     * json请求参数
     */
    private String              jsonParams;
    /**
     * URL中的变量
     */
    private Map<String, String> pathVariables;

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

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    public void setPathVariables(Map<String, String> pathVariables) {
        this.pathVariables = pathVariables;
    }
}
