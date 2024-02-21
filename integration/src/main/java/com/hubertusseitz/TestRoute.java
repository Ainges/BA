package com.hubertusseitz;

import org.apache.camel.builder.RouteBuilder;

public class TestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("undertow")
                .host("localhost")
                .port(8080)
                .contextPath("/camel");

        rest("/testroute")
                // Route to test
                .get("/test").produces("application/json").to("direct:GET_Test")
                .post("/test").consumes("application/json").to("direct:POST_Test");


        from("direct:GET_test")
                .log("Hello World!")
                .transform(simple("{\"message\": \"GET successfully called!\"}"))
                .end();

        from("direct:POST_test")
                .transform().constant("{\"message\": \"POST successfully called!\"}");
    }

}
