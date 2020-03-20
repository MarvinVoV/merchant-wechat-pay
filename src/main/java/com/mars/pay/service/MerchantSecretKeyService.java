package com.mars.pay.service;

/**
 * @author hufeng
 * @version MerchantSecretKeyService.java, v 0.1 2020/3/20 11:57 PM Exp $
 */

public interface MerchantSecretKeyService {
    /**
     * 获取商户Api证书序列号
     *
     * @return 证书序列号
     */
    String getCertSerialNo();

    /**
     * 获取商户Api私钥
     *
     * @return 私钥
     */
    String getPrivateKey();

    /**
     * 商户ApiV3秘钥
     *
     * @return 商户秘钥
     */
    String getSecretKey();

    /**
     * 获取商户号
     *
     * @return 商户号
     */
    String getMerchantId();
}
