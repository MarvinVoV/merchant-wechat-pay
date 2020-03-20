package com.mars.pay.security;

import com.mars.pay.contants.WeChatHeaderConstants;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author hufeng
 * @version WeChatPayApiV3Validator.java, v 0.1 2020/3/21 12:06 AM Exp $
 */

public class WeChatPayApiV3Validator implements Validator {
    private Verifier verifier;

    public WeChatPayApiV3Validator() {
    }

    public WeChatPayApiV3Validator(Verifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public boolean validate(CloseableHttpResponse response) throws IOException {
        Header serialNo = response.getFirstHeader(WeChatHeaderConstants.SERIAL);
        Header sign = response.getFirstHeader(WeChatHeaderConstants.SIGNATURE);
        Header timestamp = response.getFirstHeader(WeChatHeaderConstants.TIMESTAMP);
        Header nonce = response.getFirstHeader(WeChatHeaderConstants.NONCE);

        if (timestamp == null || nonce == null || serialNo == null || sign == null) {
            return false;
        }

        String message = buildMessage(response);
        return verifier.verify(serialNo.getValue(), message.getBytes(StandardCharsets.UTF_8),
                sign.getValue());
    }


    private String buildMessage(CloseableHttpResponse response) throws IOException {
        String timestamp = response.getFirstHeader(WeChatHeaderConstants.TIMESTAMP).getValue();
        String nonce = response.getFirstHeader(WeChatHeaderConstants.NONCE).getValue();

        String body = getResponseBody(response);
        return timestamp + "\n" + nonce + "\n" + body + "\n";
    }

    private String getResponseBody(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null && !entity.isRepeatable()) {
            response.setEntity(newRepeatableEntity(entity));
        }
        return entity != null ? EntityUtils.toString(response.getEntity()) : "";
    }

    private HttpEntity newRepeatableEntity(HttpEntity entity) throws IOException {
        byte[] content = EntityUtils.toByteArray(entity);
        ByteArrayEntity newEntity = new ByteArrayEntity(content);
        newEntity.setContentEncoding(entity.getContentEncoding());
        newEntity.setContentType(entity.getContentType());
        return newEntity;
    }

    public void setVerifier(Verifier verifier) {
        this.verifier = verifier;
    }
}
