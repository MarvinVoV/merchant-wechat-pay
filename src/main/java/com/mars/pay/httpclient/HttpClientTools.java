package com.mars.pay.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mars.pay.concurrent.HttpClientManager;
import com.mars.pay.model.*;
import com.mars.pay.service.SignatureCallback;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.*;

/**
 * @author hufeng
 * @version HttpClientTools.java, v 0.1 2020/3/20 11:42 PM Exp $
 */

public class HttpClientTools {
    /**
     * LOGGER
     */
    private static final Logger              LOGGER   = LoggerFactory.getLogger(HttpClientTools.class.getName());
    /**
     * boundary
     */
    public static final  String              BOUNDARY = "boundary";
    /**
     * default httpClient
     */
    private              CloseableHttpClient defaultHttpClient;

    /**
     * 初始化HttpClient实例
     */
    public void init() {
        defaultHttpClient = HttpClientManager.getInstance().getHttpClient();
    }

    /**
     * 文件上传
     *
     * @param multipartRequest     文件上传请求
     * @param signatureCallback 签名回调
     * @return response
     * @throws Throwable t
     */
    public CloseableHttpResponse doMediaUpload(MultipartRequest multipartRequest,
                                               SignatureCallback signatureCallback) throws Throwable {

        List<TextBody> textBodies = multipartRequest.getTextBodyList();
        List<BinaryBody> binaryBodies = multipartRequest.getBinaryBodies();

        HttpPost httpPost = new HttpPost(multipartRequest.getUrl());
        httpPost.addHeader(CONTENT_TYPE, MULTIPART_FORM_DATA.getMimeType());
        addHeaders(httpPost, multipartRequest.getHeaders());
        //创建MultipartEntityBuilder
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.RFC6532);
        //设置boundary
        multipartEntityBuilder.setBoundary(BOUNDARY);
        multipartEntityBuilder.setCharset(StandardCharsets.UTF_8);
        if (textBodies != null && !textBodies.isEmpty()) {
            textBodies.forEach(textBody ->
                    multipartEntityBuilder.addTextBody(
                            textBody.getName(), textBody.getText(), textBody.getContentType()));
        }
        if (binaryBodies != null && !binaryBodies.isEmpty()) {
            binaryBodies.forEach(binaryBody -> multipartEntityBuilder.addBinaryBody(
                    binaryBody.getName(), binaryBody.getData(),
                    binaryBody.getContentType(), binaryBody.getFilename()));
        }
        //放入内容
        httpPost.setEntity(multipartEntityBuilder.build());

        return doHttpClientExecute(multipartRequest, httpPost, signatureCallback);
    }

    /**
     * POST
     *
     * @param postRequest       postRequest
     * @param signatureCallback signatureCallback
     * @return response
     * @throws Throwable e
     */
    public CloseableHttpResponse doPost(PostRequest postRequest,
                                        SignatureCallback signatureCallback) throws Throwable {
        HttpPost httpPost = new HttpPost(postRequest.getUrl());
        addHeaders(httpPost, postRequest.getHeaders());
        if (StringUtils.isNotBlank(postRequest.getJsonParams())) {
            switch (postRequest.getPostContentType()) {
                case FORM:
                    httpPost.addHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED.getMimeType());
                    TreeMap<String, String> paramMap = JSON.parseObject(postRequest.getJsonParams(),
                            new TypeReference<TreeMap<String, String>>() {
                            });
                    List<NameValuePair> pairs = new ArrayList<>();
                    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
                    httpPost.setEntity(entity);
                    break;
                case JSON:
                    httpPost.addHeader(CONTENT_TYPE, APPLICATION_JSON.getMimeType());
                    StringEntity stringEntity = new StringEntity(postRequest.getJsonParams(),
                            StandardCharsets.UTF_8.toString());
                    stringEntity.setContentType(APPLICATION_JSON.getMimeType());
                    stringEntity.setContentEncoding(StandardCharsets.UTF_8.toString());
                    httpPost.setEntity(stringEntity);
                    break;
                default:
                    break;
            }
        }
        return doHttpClientExecute(postRequest, httpPost, signatureCallback);
    }

    /**
     * GET
     *
     * @param getRequest        GET请求
     * @param signatureCallback 签名回调
     * @return response
     * @throws Throwable t
     */
    public CloseableHttpResponse doGet(GetRequest getRequest,
                                       SignatureCallback signatureCallback) throws Throwable {
        HttpGet httpGet = new HttpGet(getRequest.getUrl());
        addHeaders(httpGet, getRequest.getHeaders());
        return doHttpClientExecute(getRequest, httpGet, signatureCallback);
    }

    /**
     * @param httpRequest
     * @param headers
     */
    private void addHeaders(HttpRequestBase httpRequest, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpRequest::addHeader);
        }
    }

    /**
     * 发起请求
     *
     * @param innerRequest      业务请求
     * @param httpUriRequest    http请求
     * @param signatureCallback 签名回调
     * @return response
     * @throws Exception e
     */
    private CloseableHttpResponse doHttpClientExecute(InnerRequest innerRequest,
                                                      HttpUriRequest httpUriRequest,
                                                      SignatureCallback signatureCallback) throws Exception {

        HttpUriRequest newRequest = httpUriRequest;
        if (signatureCallback != null) {
            newRequest = RequestBuilder.copy(httpUriRequest).build();
            convertToRepeatableRequestEntity(newRequest);
            signatureCallback.signature(innerRequest, newRequest);
        }

        CloseableHttpResponse response = null;
        long startTime = System.currentTimeMillis();
        try {
            response = defaultHttpClient.execute(newRequest);
            if (response != null && signatureCallback != null) {
                convertToRepeatableResponseEntity(response);
                int statusCode = response.getStatusLine().getStatusCode();
                // 对成功应答进行验签
                if (statusCode >= 200 && statusCode < 300) {
                    boolean validatorResult = signatureCallback.verifySignature(response);
                    if (!validatorResult) {
                        LOGGER.warn("验签失败，httpResponse=" + response);
                        throw new RuntimeException("验签失败");
                    }
                }
            }
        } catch (Throwable t) {
            LOGGER.info(MessageFormat.format("Http执行异常 URL:{0}, errMsg:{1}",
                    httpUriRequest.getURI().toString(), t.getMessage()));
            throw t;
        } finally {
            String result = "Y";
            String statusCode = String.valueOf(HttpStatus.SC_OK);
            if (response == null) {
                result = "N";
                statusCode = "网络异常";
            } else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                statusCode = response.getStatusLine().getStatusCode() + "";
                result = "N";
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info(MessageFormat.format("[HTTP EXEC] URL={0}, statusCode={1}, result={2}, cost={3}ms",
                    newRequest.getRequestLine().getUri(), statusCode, result, (endTime - startTime)));
        }
        return response;
    }

    private void convertToRepeatableRequestEntity(HttpUriRequest request) throws IOException {
        if (request instanceof HttpEntityEnclosingRequestBase) {
            HttpEntity entity = ((HttpEntityEnclosingRequestBase) request).getEntity();
            if (entity != null && !entity.isRepeatable()) {
                ((HttpEntityEnclosingRequestBase) request).setEntity(newRepeatableEntity(entity));
            }
        }
    }

    private HttpEntity newRepeatableEntity(HttpEntity entity) throws IOException {
        byte[] content = EntityUtils.toByteArray(entity);
        ByteArrayEntity newEntity = new ByteArrayEntity(content);
        newEntity.setContentEncoding(entity.getContentEncoding());
        newEntity.setContentType(entity.getContentType());
        return newEntity;
    }

    private void convertToRepeatableResponseEntity(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null && !entity.isRepeatable()) {
            response.setEntity(newRepeatableEntity(entity));
        }
    }
}
