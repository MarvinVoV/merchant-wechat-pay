package com.mars.pay.security;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

/**
 * @author hufeng
 * @version Validator.java, v 0.1 2020/3/20 11:58 PM Exp $
 */

public interface Validator {
    /**
     * 签名校验
     *
     * @param response response
     * @return 校验结果
     * @throws IOException e
     */
    boolean validate(CloseableHttpResponse response) throws IOException;
}
