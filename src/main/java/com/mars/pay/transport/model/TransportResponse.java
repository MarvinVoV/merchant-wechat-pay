package com.mars.pay.transport.model;

import com.mars.pay.transport.enums.TransportResultCode;

/**
 * @author hufeng
 * @version TransportResponse.java, v 0.1 2020/3/21 12:41 AM Exp $
 */

public class TransportResponse {
    private TransportResultCode transportResultCode;
    private String              jsonData;
    private String              errorDetails;

    public boolean isSuccess() {
        return transportResultCode == TransportResultCode.SUCCESS;
    }

    public void setTransportResultCode(TransportResultCode transportResultCode) {
        this.transportResultCode = transportResultCode;
    }

    public TransportResultCode getTransportResultCode() {
        return transportResultCode;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
