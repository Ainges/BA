package examples;

import org.apache.camel.builder.RouteBuilder;

public class RequestTestRoute extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {


/*        rest("/test/")
            .get()
            .to("direct:test");


        from("direct:test")
            .setBody(constant("TEST!"));

        from("timer:request?period=5000")
            .to("http://localhost:8080/test/")
                .log("${body}");*/

    }
}
