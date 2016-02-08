package com.erik.statistics;


import ratpack.server.RatpackServer;

/**
 * Created by erimol on 2016-01-31.
 */
public class ReceivingJson {

    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .get(ctx -> ctx.render("Hello World!"))
                        .get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))
                )
        );
    }
}