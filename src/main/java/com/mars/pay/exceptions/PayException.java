package com.mars.pay.exceptions;

import com.mars.pay.transport.enums.PayErrorCode;

/**
 * @author hufeng
 * @version PayException.java, v 0.1 2020/3/21 1:02 AM Exp $
 */

public class PayException extends RuntimeException {
    private String errorCode;

    public PayException(String message) {
        super(message);
    }

    public PayException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public PayException(PayErrorCode payErrorCode) {
        super(payErrorCode.getDesc());
        this.errorCode = payErrorCode.getCode();
    }
}
