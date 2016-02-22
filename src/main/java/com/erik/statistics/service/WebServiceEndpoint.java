package com.erik.statistics.service;

import ratpack.handling.Chain;
import ratpack.handling.Handler;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import ratpack.sse.ServerSentEvents;
import ratpack.stream.Streams;
import ratpack.stream.TransformablePublisher;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static ratpack.sse.ServerSentEvents.serverSentEvents;

public class WebServiceEndpoint {

    private final Map<String,Handler> handlers;

    private WebServiceEndpoint(Builder builder) {
        this.handlers = requireNonNull(builder.handlers,"handlers");
    }

    private void start() throws Exception {
        RatpackServer.start(s ->  s.serverConfig(c -> c.baseDir(BaseDir.find()))
                .handlers(chain -> {
                    Chain chainBuilder = chain.prefix("static", chain1 -> chain1.fileSystem("assets", Chain::files));
                    for (Map.Entry<String, Handler> entry : handlers.entrySet()) {
                        chainBuilder.get(entry.getKey(),entry.getValue());
                    }
                }
        ));
    }

    public static Builder newWebServiceEndPoint() {
        return new Builder();
    }

    public static class Builder {
        private Map<String,Handler> handlers = new HashMap<>();

        public void start() throws Exception {
            new WebServiceEndpoint(this).start();
        }

        public Builder multicast(Multicast multicast) {
            handlers.putAll(multicast.getHandlers());
            return this;
        }

        public Builder htmlPage(HtmlPage htmlPage) {
            handlers.putAll(htmlPage.getHandlers());
            return this;
        }

        public Builder resetService(RestService restService) {
            handlers.putAll(restService.getHandlers());
            return this;
        }
    }

    public interface HandlerMapping {
        Map<String,Handler> getHandlers();
    }




    public static class RestService<T> implements  HandlerMapping {
        private final Function<String,Object> action;
        private final String path;

        public RestService(String path,Function<String, Object> action) {
            this.action = requireNonNull(action, "action");
            this.path = requireNonNull(path, "path");
        }

        @Override
        public Map<String, Handler> getHandlers() {
            Map<String,Handler> handlerMapping = new HashMap<>();
            handlerMapping.put(path+"/:searchString",ctx -> {
                String searchString = ctx.getPathTokens().get("searchString");
                ctx.render(action.apply(searchString));
            });
            return handlerMapping;
        }

        public static PathBuilder newRestService() {
            return path -> action -> new RestService(path,action);
        }

        @FunctionalInterface
        public interface PathBuilder {
            ActionBuilder path(String path);
        }

        @FunctionalInterface
        public interface ActionBuilder {
            RestService action(Function<String,Object> action);
        }
    }

    public static class Multicast implements HandlerMapping {
        private final String publishUrl;
        private final String subscriberUrl;
        private final String eventType;

        public Multicast(String publishUrl, String subscriberUrl,String eventType) {
            this.publishUrl = requireNonNull(publishUrl,"publishUrl");
            this.subscriberUrl = requireNonNull(subscriberUrl, "subscriberUrl");
            this.eventType = requireNonNull(eventType, "eventType");
        }

        public static PublisherUrlBuilder newMulticast() {
            return publisherUrl -> subscribeUrl -> eventType ->
                    new Multicast(publisherUrl,subscribeUrl,eventType);
        }

        @Override
        public Map<String, Handler> getHandlers() {
            Map<String,Handler> handlers = new HashMap<>();
            Data data = new Data();
            TransformablePublisher<Object> multicast = Streams.multicast(subscriber -> {

                while (!data.isClosed()) {
                    try {
                        subscriber.onNext(data.getData());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }).gate(Runnable::run);

            handlers.put(publishUrl+"/:data",ctx -> {
                String input = ctx.getPathTokens().get("data");
                data.addData(input);
                ctx.getResponse().status(200).send();
            });

            handlers.put(subscriberUrl,context -> {
                ServerSentEvents events = serverSentEvents(multicast, e ->
                        e.id(Objects::toString).event(eventType).data(i -> "" + i)
                );
                context.render(events);
                data.addData("Listening on multisocket channel");
            });
            return handlers;
        }

        @FunctionalInterface
        public interface PublisherUrlBuilder {
            SubscribeUrlBuilder publisher(String publisherUrl);
        }

        @FunctionalInterface
        public interface SubscribeUrlBuilder {
            EventTypeBuilder subscriber(String subscribeUrl);
        }

        @FunctionalInterface
        public interface EventTypeBuilder {
            Multicast eventType(String eventType);
        }

    }

    public static class HtmlPage implements HandlerMapping {
        private final String path;
        private final String pagePath;

        public HtmlPage(String path, String pagePath) {
            this.path = requireNonNull(path,"path");
            this.pagePath = requireNonNull(pagePath, "pagePath");
        }

        @Override
        public Map<String, Handler> getHandlers() {
            Map<String,Handler> handlers = new HashMap<>();
            handlers.put(path,ctx -> {
                ctx.getResponse().contentType("html").sendFile(Paths.get("src/main/resources/html/", pagePath+".html"));
            });
            return handlers;
        }

        public static PathBuilder newHtmlPage() {
            return path -> pagePath -> new HtmlPage(path, pagePath);
        }

        public interface PathBuilder {
            PagePathBuilder path(String path);
        }

        public interface PagePathBuilder {
            HtmlPage pagePath(String pagePath);
        }
    }

    public static class Data {
        private BlockingQueue<String> queue = new ArrayBlockingQueue(100);
        private volatile boolean closed = false;

        public void addData(String data) {
            queue.add(data);
        }

        public String getData() throws InterruptedException {
            return queue.take();
        }

        public void setClosed() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}
