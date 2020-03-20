package com.mars.pay.service;

import java.security.cert.X509Certificate;

/**
 * 平台证书服务
 * 注：不同商户，对应的微信平台证书不一样
 *
 * @author hufeng
 * @version PlatformCertificatesService.java, v 0.1 2020/3/20 11:59 PM Exp $
 */

public interface PlatformCertificatesService {
    /**
     * 获取当前使用的平台证书
     *
     * @return cert
     */
    X509Certificate getPlatformCertificate();

    /**
     * 通过证书序列号获取证书
     *
     * @param certSerialNo serialNo
     * @return cert
     */
    X509Certificate getPlatformCertificateBySerialNo(String certSerialNo);

    /**
     * 获取机构的平台证书序列号
     *
     * @return String 序列号
     */
    String getPlatformCertificateSerialNo();
}
