package com.erik.statistics;


import com.erik.statistics.domain.NewDocument;
import com.erik.statistics.service.SearchService;
import com.erik.statistics.service.SearchServiceImpl;
import com.erik.statistics.service.WebServiceEndpoint.RestPostService;
import ratpack.exec.Promise;
import ratpack.jackson.Jackson;

import static com.erik.statistics.service.WebServiceEndpoint.HtmlPage.newHtmlPage;
import static com.erik.statistics.service.WebServiceEndpoint.Multicast.newMulticast;
import static com.erik.statistics.service.WebServiceEndpoint.RestGetService.newRestService;
import static com.erik.statistics.service.WebServiceEndpoint.newWebServiceEndPoint;

public class Main {

    private final static SearchService search = new SearchServiceImpl();

    public static void main(String[] args) throws Exception {
        newWebServiceEndPoint()
                .htmlPage(newHtmlPage().path("start").pagePath("index"))
                .htmlPage(newHtmlPage().path("learnAngular").pagePath("learnAngular"))
                .multicast(newMulticast().publisher("push").subscriber("subscribe").eventType("counter"))
                .restGetService(newRestService().path("search").action(input -> Jackson.json(search.search(input))))
                .restPostService(RestPostService.newRestService().path("newDocument").action(ctx-> {
                    Promise<NewDocument> promise = ctx.parse(Jackson.fromJson(NewDocument.class));
                    return promise.map(newDocument -> {
                        search.createDocument(newDocument);
                        return newDocument.getDocumentData();
                    });
                }))
                .start();
    }
}
