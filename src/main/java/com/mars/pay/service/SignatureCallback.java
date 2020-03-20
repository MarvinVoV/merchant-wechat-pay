package com.mars.pay.service;

import com.mars.pay.model.InnerRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author hufeng
 * @version SignatureCallback.java, v 0.1 2020/3/20 11:53 PM Exp $
 */

public interface SignatureCallback {
    /**
     * 加签
     *
     * @param innerRequest   inner请求
     * @param httpUriRequest http请求
     * @throws Exception 异常
     */
    void signature(InnerRequest innerRequest, HttpUriRequest httpUriRequest) throws Exception;

    /**
     * 验签
     *
     * @param httpResponse httpResponse
     * @return true:验证通过
     * @throws Exception e
     */
    boolean verifySignature(CloseableHttpResponse httpResponse) throws Exception;
}
