package com.mars.pay.transport;

import com.mars.pay.contants.WeChatHeaderConstants;
import com.mars.pay.exceptions.PayException;
import com.mars.pay.httpclient.HttpClientTools;
import com.mars.pay.model.InnerRequest;
import com.mars.pay.service.PlatformCertificatesService;
import com.mars.pay.service.SignatureCallback;
import com.mars.pay.transport.enums.PayErrorCode;
import com.mars.pay.transport.enums.TransportResultCode;
import com.mars.pay.transport.model.TransportRequest;
import com.mars.pay.transport.model.TransportResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author hufeng
 * @version AbstractTransport.java, v 0.1 2020/3/21 12:43 AM Exp $
 */

public abstract class AbstractTransport implements Transport {
    protected Logger LOGGER = LoggerFactory.getLogger(AbstractTransport.class);

    protected HttpClientTools httpClientTools;

    protected PlatformCertificatesService platformCertificatesService;

    protected SignatureCallback signatureCallback;

    @Override
    public TransportResponse transport(TransportRequest transportRequest) {
        TransportResponse transportResponse = null;
        try {
            preProcess(transportRequest);
            transportResponse = doTransport(transportRequest);
        } catch (Exception e) {
            LOGGER.error("数据传输发生异常", e);
        }
        return transportResponse;
    }

    protected TransportResultCode preProcess(TransportRequest transportRequest) {
        Map<String, String> pathVariables = transportRequest.getPathVariables();
        String api = transportRequest.getApi();
        String targetApi = api;

        if (pathVariables != null && !pathVariables.isEmpty()) {
            targetApi = StrSubstitutor.replace(api, pathVariables);
        }
        // replace api
        transportRequest.setApi(targetApi);
        return TransportResultCode.SUCCESS;
    }

    protected abstract TransportResponse doTransport(TransportRequest transportRequest);

    /**
     * 额外请求头信息处理
     *
     * @param transportRequest transportRequest
     * @param innerRequest     innerRequest
     */
    protected void addExtraHeaders(TransportRequest transportRequest, InnerRequest innerRequest) {
        // 包含加密信息上送时，需要微信支付平台公钥加密，证书序列号包含在请求HTTP头部的Wechatpay-Serial
        if (transportRequest.isEncrypted()) {
            String serialNo = platformCertificatesService.getPlatformCertificateSerialNo();
            if (StringUtils.isBlank(serialNo)) {
                LOGGER.error("商户序列号为空，构建请求异常");
                throw new PayException(PayErrorCode.SYSTEM_ERROR);
            }
            innerRequest.addHeader(WeChatHeaderConstants.SERIAL, serialNo);
        }
    }


    /**
     * 处理微信响应结果
     *
     * @param transportResponse transportResponse
     * @param httpResponse      httpResponse
     * @throws IOException e
     */
    protected void processResponse(TransportResponse transportResponse,
                                           CloseableHttpResponse httpResponse) throws IOException {
        if (httpResponse == null) {
            transportResponse.setErrorDetails("httpResponse为null");
            transportResponse.setTransportResultCode(TransportResultCode.HTTP_EXCEPTION);
            return;
        }
        transportResponse.setTransportResultCode(TransportResultCode.SUCCESS);
        String jsonData = EntityUtils.toString(httpResponse.getEntity());
        transportResponse.setJsonData(jsonData);
    }




    public void setHttpClientTools(HttpClientTools httpClientTools) {
        this.httpClientTools = httpClientTools;
    }

    public void setPlatformCertificatesService(PlatformCertificatesService platformCertificatesService) {
        this.platformCertificatesService = platformCertificatesService;
    }

    public void setSignatureCallback(SignatureCallback signatureCallback) {
        this.signatureCallback = signatureCallback;
    }
}
