package com.mars.pay.utils;

import com.mars.pay.contants.WeChatHeaderConstants;
import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * @author hufeng
 * @version WeChatPayApiV3Util.java, v 0.1 2020/3/21 12:02 AM Exp $
 */

public class WeChatPayApiV3Util {
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM  = new SecureRandom();

    /**
     * 计算签名值
     *
     * @param message    签名串
     * @param privateKey 私钥
     * @return 签名值
     */
    private static String sign(byte[] message, String privateKey) throws Exception {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(getPrivateKey(privateKey));
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException
                | InvalidKeySpecException | IOException e) {
            throw e;
        }
    }

    /**
     * 构造签名串
     *
     * @param method    请求方法 POST, GET 等
     * @param targetUrl encode过的url
     * @param timestamp 时间戳
     * @param nonceStr  随机字符串
     * @param body      消息体
     * @return 签名串
     */
    private static String buildMessage(String method, String targetUrl, long timestamp,
                                       String nonceStr, String body) {
        return method + "\n" + targetUrl + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n";
    }

    /**
     * 获取签名信息
     *
     * @param merchantId   发起请求的商户（包括直连商户、服务商或渠道商）的商户号
     * @param privateKey   商户私钥
     * @param certSerialNo 商户API证书序列号
     * @param url          请求URL(encode过的）
     * @param method       请求方法 POST or GET
     * @param body         请求消息体
     * @return 签名信息
     */
    private static String getToken(String merchantId, String privateKey, String certSerialNo,
                                   String url, String method, String body) throws Exception {
        String nonceStr = generateNonceStr();
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, url, timestamp, nonceStr, body);
        String signature = sign(message.getBytes(StandardCharsets.UTF_8), privateKey);
        return "mchid=\"" + merchantId + "\"," + "nonce_str=\"" + nonceStr + "\"," + "timestamp=\""
                + timestamp + "\"," + "serial_no=\"" + certSerialNo + "\"," + "signature=\""
                + signature + "\"";
    }

    /**
     * 微信支付商户API v3要求请求通过HTTPAuthorization头来传递签名。Authorization由认证类型和签名信息两个部分组成。
     * Authorization: 认证类型 签名信息
     * <p>
     * 认证类型，目前为WECHATPAY2-SHA256-RSA2048
     *
     * @param merchantId   发起请求的商户（包括直连商户、服务商或渠道商）的商户号
     * @param privateKey   商户私钥
     * @param certSerialNo 商户API证书序列号
     * @param url          请求URL(encode过的）
     * @param method       请求方法 POST or GET
     * @param body         请求消息体
     * @return Authorization
     */
    public static String getAuthorization(String merchantId, String privateKey, String certSerialNo,
                                          String url, String method, String body) throws Exception {
        return WeChatHeaderConstants.SCHEMA
                + getToken(merchantId, privateKey, certSerialNo, url, method, body);
    }

    /**
     * 获取私钥对象
     *
     * @param key 私钥
     * @return 私钥对象
     */
    private static PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException,
            IOException, InvalidKeySpecException {
        try {
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] b = base64decoder.decodeBuffer(key);
            PKCS8EncodedKeySpec x509EncodedKeySpec = new PKCS8EncodedKeySpec(b);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(x509EncodedKeySpec);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 敏感信息加密
     * {link https://wechatpay-api.gitbook.io/wechatpay-api-v3/qian-ming-zhi-nan-1/min-gan-xin-xi-jia-mi}
     *
     * @param message     要加密的信息
     * @param certificate 微信支付平台公钥（证书)
     * @return 加密后的信息
     * @throws IllegalBlockSizeException e
     */
    public static String rsaEncryptOAEP(String message,
                                        X509Certificate certificate) throws IllegalBlockSizeException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            byte[] cipherData = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(cipherData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的证书", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
        }
    }
}
