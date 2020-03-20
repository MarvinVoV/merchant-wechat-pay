package com.mars.pay.enums;

/**
 * @author hufeng
 * @version PostContentType.java, v 0.1 2020/3/20 11:50 PM Exp $
 */

public enum PostContentType {
    JSON("JSON", "请求数据为json格式"),
    FORM("FORM", "FORM表单格式");

    private String code;
    private String desc;

    PostContentType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
