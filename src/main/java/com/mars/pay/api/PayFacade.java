package com.mars.pay.api;

import com.mars.pay.api.request.EncryptRequest;
import com.mars.pay.api.request.ParamsRequest;
import com.mars.pay.api.request.UploadRequest;
import com.mars.pay.api.response.ApiResult;

import java.util.Map;

/**
 * @author hufeng
 * @version PayFacade.java, v 0.1 2020/3/21 12:26 AM Exp $
 */

public interface PayFacade {
    /**
     * 敏感信息加密
     *
     * @param request request, k:field name, v: target value
     * @return k: field name, v: encrypted value
     */
    ApiResult<Map<String, String>> encrypt(EncryptRequest request);

    /**
     * invoke service
     *
     * @param request request
     * @return result
     */
    ApiResult<String> invokeService(ParamsRequest request);

    /**
     * upload picture or file
     *
     * @param uploadRequest request
     * @return k: filename, v: response data
     */
    ApiResult<Map<String, String>> upload(UploadRequest uploadRequest);
}
