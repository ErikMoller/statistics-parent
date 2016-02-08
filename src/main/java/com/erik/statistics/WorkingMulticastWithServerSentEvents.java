package com.erik.statistics;

import ratpack.handling.Chain;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import ratpack.sse.ServerSentEvents;
import ratpack.stream.Streams;
import ratpack.stream.TransformablePublisher;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static ratpack.sse.ServerSentEvents.serverSentEvents;

/**
 * Created by erimol on 2016-02-06.
 */
public class WorkingMulticastWithServerSentEvents {
    public static void main(String[] args) throws Exception {
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
        RatpackServer.start(s ->  s.serverConfig(c -> c.baseDir(BaseDir.find()))
                .handlers(chain ->
                        chain.prefix("static",chain1-> chain1.fileSystem("assets", Chain::files))
                                .get("subscribe",context -> {
                            ServerSentEvents events = serverSentEvents(multicast, e ->
                                    e.id(Objects::toString).event("counter").data(i -> ""+i)
                            );
                            context.render(events);
                            data.addData("Listening on multisocket channel");
                        }).get("push/:data",ctx -> {
                                    String input = ctx.getPathTokens().get("data");
                                    data.addData(input);
                                    ctx.getResponse().status(200).send();
                        }).get("start",ctx -> {
                            ctx.getResponse().contentType("html").sendFile(Paths.get("src/main/resources/html/", "index.html"));
                        })));
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
