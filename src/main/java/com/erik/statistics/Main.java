package com.erik.statistics;


import static com.erik.statistics.service.WebServiceEndpoint.HtmlPage.newHtmlPage;
import static com.erik.statistics.service.WebServiceEndpoint.Multicast.newMulticast;
import static com.erik.statistics.service.WebServiceEndpoint.RestService.newRestService;
import static com.erik.statistics.service.WebServiceEndpoint.newWebServiceEndPoint;

public class Main {

    public static void main(String[] args) throws Exception {
        newWebServiceEndPoint()
                .htmlPage(newHtmlPage().path("start").pagePath("index"))
                .htmlPage(newHtmlPage().path("learnAngular").pagePath("learnAngular"))
                .multicast(newMulticast().publisher("push").subscriber("subscribe").eventType("counter"))
                .resetService(newRestService().path("search").action(input -> "Result from server new: " + input))
                .start();
    }
}
