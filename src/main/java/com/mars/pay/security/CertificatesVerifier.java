package com.mars.pay.security;

import com.mars.pay.service.PlatformCertificatesService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * @author hufeng
 * @version CertificatesVerifier.java, v 0.1 2020/3/20 11:59 PM Exp $
 */

public class CertificatesVerifier implements Verifier {

    private PlatformCertificatesService platformCertificatesService;

    /**
     * 验签
     *
     * @param certificate 微信支付平台证书
     * @param message     message
     * @param signature   应答签名
     * @return verify
     */
    private boolean verify(X509Certificate certificate, byte[] message, String signature) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(certificate);
            sign.update(message);
            return sign.verify(Base64.getDecoder().decode(signature));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持SHA256withRSA", e);
        } catch (SignatureException e) {
            throw new RuntimeException("签名验证过程发生了错误", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("无效的证书", e);
        }
    }

    /**
     * 验签
     *
     * @param serialNumber 证书序列化
     * @param message      message
     * @param signature    应答签名
     * @return verify
     */
    @Override
    public boolean verify(String serialNumber, byte[] message, String signature) {
        X509Certificate certificate = platformCertificatesService
                .getPlatformCertificateBySerialNo(serialNumber);
        return verify(certificate, message, signature);
    }

    public void setPlatformCertificatesService(PlatformCertificatesService platformCertificatesService) {
        this.platformCertificatesService = platformCertificatesService;
    }

}
