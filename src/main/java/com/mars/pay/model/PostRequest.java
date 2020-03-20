package com.mars.pay.model;

import com.mars.pay.enums.PostContentType;

/**
 * @author hufeng
 * @version PostRequest.java, v 0.1 2020/3/20 11:50 PM Exp $
 */

public class PostRequest extends InnerRequest {
    /**
     * content type
     */
    private PostContentType postContentType;
    /**
     * 参数 一般为json数据
     */
    private String          jsonParams;

    public PostContentType getPostContentType() {
        return postContentType;
    }

    public void setPostContentType(PostContentType postContentType) {
        this.postContentType = postContentType;
    }

    public String getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(String jsonParams) {
        this.jsonParams = jsonParams;
    }
}
