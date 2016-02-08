package com.erik.statistics;

import ratpack.server.RatpackServer;
import ratpack.test.embed.EmbeddedApp;
import ratpack.jackson.Jackson;
import ratpack.stream.Streams;
import ratpack.http.client.ReceivedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;

import java.util.Arrays;

import static ratpack.jackson.Jackson.toJson;
import static ratpack.sse.ServerSentEvents.serverSentEvents;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;

/**
 * Created by erimol on 2016-02-06.
 */
public class JsonStream {

    public static class Person {
        private final String name;
        public Person(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public static void main(String... args) throws Exception {
        RatpackServer.start(s -> s
                .handlers(chain -> chain
                        .get("stream", ctx -> {
                            Publisher<Person> people = Streams.publish(Arrays.asList(
                                    new Person("a"),
                                    new Person("b"),
                                    new Person("c")
                            ));

                            ctx.render(serverSentEvents(people, e -> e.data(toJson(ctx))));
                        })
                )
        );
    }
}
