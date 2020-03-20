package com.mars.pay.transport.impl;

import com.mars.pay.enums.TransportExtKeyEnum;
import com.mars.pay.model.BinaryBody;
import com.mars.pay.model.MultipartRequest;
import com.mars.pay.model.TextBody;
import com.mars.pay.transport.AbstractTransport;
import com.mars.pay.transport.enums.TransportResultCode;
import com.mars.pay.transport.model.TransportRequest;
import com.mars.pay.transport.model.TransportResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * @author hufeng
 * @version WeChatMultipartTransport.java, v 0.1 2020/3/21 1:09 AM Exp $
 */

public class WeChatMultipartTransport extends AbstractTransport {
    @Override
    protected TransportResponse doTransport(TransportRequest transportRequest) {
        TransportResponse transportResponse = new TransportResponse();
        transportResponse.setTransportResultCode(TransportResultCode.SUCCESS);

        CloseableHttpResponse httpResponse = null;
        try {
            String fileName = transportRequest.getFileName();
            byte[] binaryData = transportRequest.getContent();
            // 文件sha256
            String fileSha256 = DigestUtils.sha256Hex(new ByteArrayInputStream(binaryData));
            String metaBody = "{\"filename\":\"" + fileName + "\",\"sha256\":\"" + fileSha256
                    + "\"}";

            MultipartRequest uploadRequest = new MultipartRequest();
            uploadRequest.setUrl(transportRequest.getApi());
            uploadRequest.addHeader(ACCEPT, APPLICATION_JSON.getMimeType());

            uploadRequest.addTextBody(new TextBody("meta", metaBody, ContentType.APPLICATION_JSON));
            uploadRequest.addBinaryBody(
                    new BinaryBody("file", binaryData, fileName, ContentType.IMAGE_JPEG));
            // 签名消息体 不从request中取，直接传
            uploadRequest.putExtInfo(TransportExtKeyEnum.WECHAT_MEDIA_META_BODY.getCode(),
                    metaBody);

            addExtraHeaders(transportRequest, uploadRequest);

            httpResponse = httpClientTools.doMediaUpload(uploadRequest, signatureCallback);
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
