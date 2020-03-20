package com.mars.pay.api.iml;

import com.mars.pay.api.PayFacade;
import com.mars.pay.api.request.EncryptRequest;
import com.mars.pay.api.request.ParamsRequest;
import com.mars.pay.api.request.Request;
import com.mars.pay.api.request.UploadRequest;
import com.mars.pay.api.response.ApiResult;
import com.mars.pay.model.ApiConfig;
import com.mars.pay.service.ApiConfigService;
import com.mars.pay.transport.Transport;
import com.mars.pay.transport.TransportFactory;
import com.mars.pay.transport.model.TransportRequest;
import com.mars.pay.transport.model.TransportResponse;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;

import java.util.Map;

/**
 * @author hufeng
 * @version PayFacadeImpl.java, v 0.1 2020/3/21 12:33 AM Exp $
 */

public class PayFacadeImpl implements PayFacade {

    private ApiConfigService apiConfigService;
    private TransportFactory transportFactory;

    @Override
    public ApiResult<Map<String, String>> encrypt(EncryptRequest request) {
        return null;
    }

    @Override
    public ApiResult<String> invokeService(ParamsRequest request) {
        ApiConfig apiConfig = apiConfigService.getApiConfigById(request.getId());
        Transport transport = transportFactory.getTransport(request);
        TransportRequest transportRequest = buildTransportRequest(apiConfig, request);
        TransportResponse transportResponse = transport.transport(transportRequest);
        return buildResult(transportResponse);
    }

    private TransportRequest buildTransportRequest(ApiConfig apiConfig, Request request) {
        // todo
        return null;
    }

    private ApiResult<String> buildResult(TransportResponse transportResponse) {
        // todo
        return null;
    }

    @Override
    public ApiResult<Map<String, String>> upload(UploadRequest uploadRequest) {
        return null;
    }

    public void setApiConfigService(ApiConfigService apiConfigService) {
        this.apiConfigService = apiConfigService;
    }

    public void setTransportFactory(TransportFactory transportFactory) {
        this.transportFactory = transportFactory;
    }
}
