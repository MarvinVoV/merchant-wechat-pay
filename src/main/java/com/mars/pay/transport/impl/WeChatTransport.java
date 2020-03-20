package com.mars.pay.transport.impl;

import com.mars.pay.enums.PostContentType;
import com.mars.pay.model.GetRequest;
import com.mars.pay.model.PostRequest;
import com.mars.pay.transport.AbstractTransport;
import com.mars.pay.transport.enums.TransportResultCode;
import com.mars.pay.transport.model.TransportRequest;
import com.mars.pay.transport.model.TransportResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * @author hufeng
 * @version WechatTransport.java, v 0.1 2020/3/21 12:55 AM Exp $
 */

public class WeChatTransport extends AbstractTransport {
    @Override
    protected TransportResponse doTransport(TransportRequest transportRequest) {
        TransportResponse transportResponse = new TransportResponse();
        transportResponse.setTransportResultCode(TransportResultCode.SUCCESS);

        String paramJson = transportRequest.getJsonParams();
        CloseableHttpResponse httpResponse = null;
        try {
            if (StringUtils.equals(HttpGet.METHOD_NAME, transportRequest.getRequestType())) {
                GetRequest getRequest = new GetRequest();
                getRequest.setUrl(transportRequest.getApi());
                getRequest.addHeader(ACCEPT, APPLICATION_JSON.getMimeType());
                addExtraHeaders(transportRequest, getRequest);
                httpResponse = httpClientTools.doGet(getRequest, signatureCallback);
            } else {
                PostRequest postRequest = new PostRequest();
                postRequest.setUrl(transportRequest.getApi());
                postRequest.setPostContentType(PostContentType.JSON);
                postRequest.setJsonParams(paramJson);
                postRequest.addHeader(ACCEPT, APPLICATION_JSON.getMimeType());
                addExtraHeaders(transportRequest, postRequest);
                httpResponse = httpClientTools.doPost(postRequest, signatureCallback);
            }
            // 结果处理
            processResponse(transportResponse, httpResponse);
        } catch (Throwable e) {
            LOGGER.error("[Transport] 通信未知异常,url=" + transportRequest.getApi(), e);
            transportResponse.setErrorDetails("通信未知异常,e=" + e.getMessage());
            transportResponse.setTransportResultCode(TransportResultCode.HTTP_EXCEPTION);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    LOGGER.error("http Response 关闭异常", e);
                }
            }
            LOGGER.info("传输结果transportResponse=" + transportResponse);
        }
        return transportResponse;
    }
}
