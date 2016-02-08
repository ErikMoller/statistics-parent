package com.erik.statistics;


import ratpack.server.RatpackServer;

import static ratpack.jackson.Jackson.json;

public class WorkingWithJson {

    public static class Person {
        private final String name;
        private final Integer age;
        public Person(String name, Integer age)
        {
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }

    public static void main(String... args) throws Exception {
        RatpackServer.start(s -> s
                .handlers(chain ->
                        chain.get(ctx -> ctx.render(json(new Person("John",24))))
                )
        );
    }
}
