package com.erik.statistics.domain;

import java.io.Serializable;

/**
 * Created by erimol on 2016-02-22.
 * JSON data
 */
public class NewDocument implements Serializable {

    private final static long serialVersionUID = 1L;

    private String documentData;

    private NewDocument() {
    }

    public String getDocumentData() {
        return documentData;
    }
}
