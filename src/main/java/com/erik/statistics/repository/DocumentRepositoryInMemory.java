package com.erik.statistics.repository;

import com.erik.statistics.domain.Document;
import com.erik.statistics.domain.DocumentId;
import com.erik.statistics.domain.NewDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by erimol on 2016-02-22.
 */
public class DocumentRepositoryInMemory implements DocmentRepository {

    private final Map<DocumentId,Document> data = new HashMap<>();

    @Override
    public void create(NewDocument newDocument) {
        Document document = Document.valueOf(DocumentId.valueOf(UUID.randomUUID().getMostSignificantBits()), newDocument.getDocumentData());
        data.put(document.getId(),document);
    }

    @Override
    public List<Document> findDocument(String searchData) {
        return data.values().stream()
                .filter(document -> document.getData().contains(searchData))
                .collect(Collectors.toList());
    }
}
