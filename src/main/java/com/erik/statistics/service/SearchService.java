package com.erik.statistics.service;


import com.erik.statistics.domain.Document;

import java.util.List;

public interface SearchService {

    List<Document> search(String searchString);

}
