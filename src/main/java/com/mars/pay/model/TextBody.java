package com.mars.pay.model;

import org.apache.http.entity.ContentType;

/**
 * @author hufeng
 * @version TextBody.java, v 0.1 2020/3/20 11:46 PM Exp $
 */
public class TextBody {
    private String      name;
    private String      text;
    private ContentType contentType;

    public TextBody(String name, String text, ContentType contentType) {
        this.name = name;
        this.text = text;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
