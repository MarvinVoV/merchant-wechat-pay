package com.mars.pay.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author hufeng
 * @version RequestType.java, v 0.1 2020/3/21 12:17 AM Exp $
 */

public enum RequestType {
    GET, POST;

    public RequestType getByName(String name) {
        for (RequestType item : RequestType.values()) {
            if (StringUtils.equals(name, item.name())) {
                return item;
            }
        }
        return null;
    }
}
