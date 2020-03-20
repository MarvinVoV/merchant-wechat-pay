package com.mars.pay.api.request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hufeng
 * @version EncrptRequest.java, v 0.1 2020/3/21 12:27 AM Exp $
 */

public class EncryptRequest extends Request {
    private Map<String, String> params = new HashMap<>();

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
