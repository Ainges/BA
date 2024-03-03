package examples;

import org.apache.camel.builder.RouteBuilder;

public class TimerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        from("timer:foo?period=1000")
//                .id("timer-route")
//                .log("Hello World");
    }
}