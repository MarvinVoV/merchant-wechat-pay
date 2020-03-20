package com.mars.pay.transport;

import com.mars.pay.api.request.ParamsRequest;
import com.mars.pay.api.request.Request;
import com.mars.pay.api.request.UploadRequest;
import com.mars.pay.transport.impl.WeChatMultipartTransport;
import com.mars.pay.transport.impl.WeChatTransport;

/**
 * @author hufeng
 * @version TransportFactory.java, v 0.1 2020/3/21 1:16 AM Exp $
 */

public class TransportFactory {
    private WeChatMultipartTransport weChatMultipartTransport;
    private WeChatTransport          weChatTransport;

    public Transport getTransport(Request request) {
        if (request instanceof ParamsRequest) {
            return weChatTransport;
        }
        if (request instanceof UploadRequest) {
            return weChatMultipartTransport;
        }
        return null;
    }

    public void setWeChatMultipartTransport(WeChatMultipartTransport weChatMultipartTransport) {
        this.weChatMultipartTransport = weChatMultipartTransport;
    }

    public void setWeChatTransport(WeChatTransport weChatTransport) {
        this.weChatTransport = weChatTransport;
    }
}
