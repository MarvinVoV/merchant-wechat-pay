package com.mars.pay.transport.enums;

/**
 * @author hufeng
 * @version PayErrorCode.java, v 0.1 2020/3/21 1:03 AM Exp $
 */

public enum PayErrorCode {
    SUCCESS("SUCCESS", "成功"),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),
    HTTP_EXCEPTION("HTTP_EXCEPTION", "http exception");

    PayErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
