package com.mars.pay.api.request;

/**
 * @author hufeng
 * @version UploadRequest.java, v 0.1 2020/3/21 12:30 AM Exp $
 */

public class UploadRequest extends Request{
    private String fileName;
    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
