package com.erik.statistics.service;


import com.erik.statistics.domain.Document;
import com.erik.statistics.domain.NewDocument;

import java.util.List;

public interface SearchService {

    List<Document> search(String searchString);

    void createDocument(NewDocument newDocument);

}
