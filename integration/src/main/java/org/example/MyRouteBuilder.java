package org.example;

import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .component("undertow")
            .host("localhost")
            .port(8080)
            .contextPath("/api");


        rest("/say")
                .get("/hello").to("direct:hello")
                .get("/bye").consumes("application/json").to("direct:bye")
                .post("/bye").to("log:Post Called!");

        from("direct:hello")
                .log("Hello World!")
                .transform().constant("Hello World");

        from("direct:bye")
                .transform().constant("Bye World");
    }

}
