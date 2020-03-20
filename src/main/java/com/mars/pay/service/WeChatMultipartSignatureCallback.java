package com.mars.pay.service;

import com.mars.pay.enums.TransportExtKeyEnum;
import com.mars.pay.model.InnerRequest;
import com.mars.pay.security.Validator;
import com.mars.pay.utils.WeChatPayApiV3Util;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;

/**
 * @author hufeng
 * @version WeChatMultipartSignatureCallback.java, v 0.1 2020/3/21 12:12 AM Exp $
 */

public class WeChatMultipartSignatureCallback implements SignatureCallback {
    private MerchantSecretKeyService merchantSecretKeyService;
    private Validator                validator;

    @Override
    public void signature(InnerRequest innerRequest, HttpUriRequest httpUriRequest) throws Exception {
        URI uri = httpUriRequest.getURI();
        String targetUrl = uri.getRawPath();
        if (uri.getQuery() != null) {
            targetUrl += "?" + uri.getRawQuery();
        }
        String method = httpUriRequest.getRequestLine().getMethod();

        String merchantId =merchantSecretKeyService.getMerchantId();
        String certSerialNo = merchantSecretKeyService.getCertSerialNo();
        String privateKey = merchantSecretKeyService.getPrivateKey();

        String signContent = innerRequest.fetchExtInfo(TransportExtKeyEnum.WECHAT_MEDIA_META_BODY.getCode());

        String authorization = WeChatPayApiV3Util.getAuthorization(merchantId, privateKey,
                certSerialNo, targetUrl, method, signContent);
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
