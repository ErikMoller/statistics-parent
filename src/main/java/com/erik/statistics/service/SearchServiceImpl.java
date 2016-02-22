package com.erik.statistics.service;

import com.erik.statistics.domain.Document;
import com.erik.statistics.domain.DocumentId;
import com.erik.statistics.domain.NewDocument;
import com.erik.statistics.repository.DocmentRepository;
import com.erik.statistics.repository.DocumentRepositoryInMemory;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by erimol on 2016-02-21.
 */
public class SearchServiceImpl implements SearchService {

    private final DocmentRepository repository = new DocumentRepositoryInMemory();


    @Override
    public List<Document> search(String searchString) {
        return repository.findDocument(searchString);
    }

    @Override
    public void createDocument(NewDocument newDocument) {
        repository.create(newDocument);
    }


}
