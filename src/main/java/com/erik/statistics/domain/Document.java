package com.erik.statistics.domain;

import com.google.common.hash.HashCode;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.*;

/**
 * Created by erimol on 2016-02-22.
 */
public class Document implements Serializable {

    private final static long serialVersionUID = 1L;

    private final DocumentId id;
    private final String data;

    private Document(DocumentId id, String data) {
        this.id = requireNonNull(id,"id");
        this.data = requireNonNull(data, "data");
    }

    public static Document valueOf(DocumentId id, String data) {
        return new Document(id,data);
    }

    public DocumentId getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    @Override
    public int hashCode() {
        return (int)id.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
