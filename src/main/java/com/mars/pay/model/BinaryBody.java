package com.mars.pay.model;

import org.apache.http.entity.ContentType;

/**
 * @author hufeng
 * @version BinaryBody.java, v 0.1 2020/3/20 11:47 PM Exp $
 */

public class BinaryBody {
    private String      name;
    private byte[]      data;
    private String      filename;
    private ContentType contentType;

    public BinaryBody(String name, byte[] data, String filename, ContentType contentType) {
        this.name = name;
        this.data = data;
        this.filename = filename;
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public String getFilename() {
        return filename;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
