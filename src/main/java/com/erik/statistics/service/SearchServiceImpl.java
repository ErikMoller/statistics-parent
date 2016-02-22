package com.erik.statistics.service;

import com.erik.statistics.domain.Document;
import com.erik.statistics.domain.DocumentId;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by erimol on 2016-02-21.
 */
public class SearchServiceImpl implements SearchService {


    @Override
    public List<Document> search(String searchString) {
        return Stream.of(Document.valueOf(DocumentId.valueOf(1L),"ett"),Document.valueOf(DocumentId.valueOf(2L),"två")).collect(Collectors.toList());
    }
}
