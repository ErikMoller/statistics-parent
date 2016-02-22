package com.erik.statistics.domain;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.*;

/**
 * Created by erimol on 2016-02-22.
 */
public class DocumentId implements Serializable {

    private final static long serialVersionUID = 1L;

    private final long id;

    public DocumentId(long id) {
        this.id = requireNonNull(id, "id");
    }

    public static DocumentId valueOf(long id) {
        return new DocumentId(id);
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
