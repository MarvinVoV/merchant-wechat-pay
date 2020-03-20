package com.mars.pay.transport.enums;

/**
 * @author hufeng
 * @version TransportResultCode.java, v 0.1 2020/3/21 12:45 AM Exp $
 */

public enum TransportResultCode {
    SUCCESS("SUCCESS", "成功", null),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常", PayErrorCode.SYSTEM_ERROR),
    HTTP_EXCEPTION("HTTP_EXCEPTION", "http exception", PayErrorCode.HTTP_EXCEPTION);

    TransportResultCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    TransportResultCode(String code, String desc, PayErrorCode payErrorCode) {
        this.code = code;
        this.desc = desc;
        this.payErrorCode = payErrorCode;
    }

    private String       code;
    private String       desc;
    private PayErrorCode payErrorCode;

    public PayErrorCode getPayErrorCode() {
        return payErrorCode;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
