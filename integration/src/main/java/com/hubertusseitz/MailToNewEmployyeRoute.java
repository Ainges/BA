package com.hubertusseitz;

import org.apache.camel.builder.RouteBuilder;

public class MailToNewEmployyeRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("undertow")
                .host("localhost")
                .port(8080)
                .contextPath("/camel");

        rest("/mailToNewEmployee")
                // Route to test
                .post("/").consumes("application/json").to("direct:POST_MailToNewEmployee");

        from("direct:POST_MailToNewEmployee")
                .log("Mail to new employee")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getIn().setBody(body);
                });
    }
}
