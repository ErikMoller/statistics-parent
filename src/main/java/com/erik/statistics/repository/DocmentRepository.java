package com.erik.statistics.repository;

import com.erik.statistics.domain.Document;
import com.erik.statistics.domain.NewDocument;

import java.util.List;

/**
 * Created by erimol on 2016-02-22.
 */
public interface DocmentRepository {

    void create(NewDocument newDocument);

    List<Document> findDocument(String searchData);
}
