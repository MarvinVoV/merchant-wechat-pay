package com.mars.pay.enums;

/**
 * @author hufeng
 * @version TransportExtKeyEnum.java, v 0.1 2020/3/21 1:11 AM Exp $
 */

public enum TransportExtKeyEnum {
    WECHAT_MEDIA_META_BODY("WECHAT_MEDIA_META_BODY", "签名消息体");

    private String code;
    private String desc;

    TransportExtKeyEnum(String code, String desc) {
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
