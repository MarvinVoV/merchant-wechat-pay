package com.mars.pay.utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * AES-256-GCM 解密工具类
 *
 * @author hufeng
 * @version AesUtils.java, v 0.1 2020/3/21 12:05 AM Exp $
 * 微信证书和回调报文解密
 * {link https://wechatpay-api.gitbook.io/wechatpay-api-v3/qian-ming-zhi-nan-1/zheng-shu-he-hui-tiao-bao-wen-jie-mi}
 */

public class AesUtils {
    /**
     * Api V3 KEY 长度
     */
    private static final int    KEY_LENGTH_BYTE = 32;
    /**
     * TAG LENGTH
     */
    private static final int    TAG_LENGTH_BIT  = 128;
    /**
     * APIv3密钥
     */
    private final        byte[] aesKey;

    static {
        Security.setProperty("crypto.policy", "unlimited");
    }

    public AesUtils(byte[] key) {
        if (key.length != KEY_LENGTH_BYTE) {
            throw new IllegalArgumentException("无效的ApiV3Key，长度必须为32个字节");
        }
        this.aesKey = key;
    }

    /**
     * @param associatedData 关联数据
     * @param nonce          随机字符串
     * @param cipherText     密文
     * @return 明文
     * @throws GeneralSecurityException e
     */
    public String decryptToString(byte[] associatedData, byte[] nonce,
                                  String cipherText) throws GeneralSecurityException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)),
                    StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
