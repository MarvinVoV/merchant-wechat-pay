package com.mars.pay.transport;

import com.mars.pay.transport.model.TransportRequest;
import com.mars.pay.transport.model.TransportResponse;

/**
 * @author hufeng
 * @version Transport.java, v 0.1 2020/3/21 12:42 AM Exp $
 */

public interface Transport {

    TransportResponse transport(TransportRequest transportRequest);

}
