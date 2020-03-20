package com.mars.pay.security;

/**
 * @author hufeng
 * @version Verifier.java, v 0.1 2020/3/20 11:58 PM Exp $
 */

public interface Verifier {
    /**
     * 验签
     *
     * @param serialNumber 证书序列号
     * @param message      message
     * @param signature    签名值
     * @return 校验结果
     */
    boolean verify(String serialNumber, byte[] message, String signature);
}
