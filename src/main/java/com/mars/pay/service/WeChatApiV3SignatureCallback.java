package com.mars.pay.service;

import com.mars.pay.model.InnerRequest;
import com.mars.pay.security.Validator;
import com.mars.pay.utils.WeChatPayApiV3Util;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.net.URI;

/**
 * @author hufeng
 * @version WeChatApiV3SignatureCallback.java, v 0.1 2020/3/20 11:57 PM Exp $
 */

public class WeChatApiV3SignatureCallback implements SignatureCallback {
    /**
     * 商户秘钥服务
     */
    private MerchantSecretKeyService merchantSecretKeyService;
    /**
     * 验签服务
     */
    private Validator                validator;

    @Override
    public void signature(InnerRequest innerRequest, HttpUriRequest httpUriRequest) throws Exception {
        URI uri = httpUriRequest.getURI();
        String targetUrl = uri.getRawPath();
        if (uri.getQuery() != null) {
            targetUrl += "?" + uri.getRawQuery();
        }
        String method = httpUriRequest.getRequestLine().getMethod();

        String body = "";
        // PATCH,POST,PUT
        if (httpUriRequest instanceof HttpEntityEnclosingRequestBase) {
            body = EntityUtils
                    .toString(((HttpEntityEnclosingRequestBase) httpUriRequest).getEntity());
        }
        String merchantId = merchantSecretKeyService.getMerchantId();
        String certSerialNo = merchantSecretKeyService.getCertSerialNo();
        String privateKey = merchantSecretKeyService.getPrivateKey();

        String authorization = WeChatPayApiV3Util.getAuthorization(merchantId, privateKey,
                certSerialNo, targetUrl, method, body);
        httpUriRequest.addHeader("Authorization", authorization);
    }

    @Override
    public boolean verifySignature(CloseableHttpResponse httpResponse) throws Exception {
        return validator.validate(httpResponse);
    }

    public void setMerchantSecretKeyService(MerchantSecretKeyService merchantSecretKeyService) {
        this.merchantSecretKeyService = merchantSecretKeyService;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
