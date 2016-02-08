package com.erik.statistics;

import org.junit.Assert;
import ratpack.registry.Registry;
import ratpack.test.embed.EmbeddedApp;

import static org.junit.Assert.*;

/**
 * Created by erimol on 2016-01-31.
 */
public class ContextualObjectMain {

    public static void main(String... args) throws Exception {
        EmbeddedApp
                .fromHandlers(chain -> chain
                        .prefix("person/:id", (personChain) -> personChain
                                .all(ctx -> {
                                    String id = ctx.getPathTokens().get("id"); // (1)
                                    Person person = new PersonImpl(id, "example-status", "example-age");
                                    ctx.next(Registry.single(Person.class, person)); // (2)
                                })
                                .get("status", ctx -> {
                                    Person person = ctx.get(Person.class); // (3)
                                    ctx.render("person " + person.getId() + " status: " + person.getStatus());
                                })
                                .get("age", ctx -> {
                                    Person person = ctx.get(Person.class); // (4)
                                    ctx.render("person " + person.getId() + " age: " + person.getAge());
                                }))
                )
                .test(httpClient -> {
                    assertEquals("person 10 status: example-status", httpClient.get("person/10/status").getBody().getText());
                    assertEquals("person 6 age: example-age", httpClient.get("person/6/age").getBody().getText());
                });
    }

    public static interface Person {
        String getId();

        String getStatus();

        String getAge();
    }

    public static class PersonImpl implements Person {
        private final String id;
        private final String status;
        private final String age;

        public PersonImpl(String id, String status, String age) {
            this.id = id;
            this.status = status;
            this.age = age;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getStatus() {
            return status;
        }

        @Override
        public String getAge() {
            return age;
        }
    }
}
