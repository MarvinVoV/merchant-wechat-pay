package com.mars.pay.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hufeng
 * @version MultipartRequest.java, v 0.1 2020/3/20 11:46 PM Exp $
 */

public class MultipartRequest extends InnerRequest{
    /**
     * binary body
     */
    private List<TextBody>   textBodyList;
    /**
     * text body
     */
    private List<BinaryBody> binaryBodies;

    public void addTextBody(TextBody textBody) {
        if (textBody == null) {
            return;
        }
        if (textBodyList == null) {
            textBodyList = new ArrayList<>();
        }
        textBodyList.add(textBody);
    }

    public void addBinaryBody(BinaryBody binaryBody) {
        if (binaryBody == null) {
            return;
        }
        if (binaryBodies == null) {
            binaryBodies = new ArrayList<>();
        }
        binaryBodies.add(binaryBody);
    }

    public List<TextBody> getTextBodyList() {
        return textBodyList;
    }

    public List<BinaryBody> getBinaryBodies() {
        return binaryBodies;
    }
}
