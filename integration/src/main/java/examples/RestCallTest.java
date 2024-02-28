package examples;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class RestCallTest extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Eine Route welche mit Direct Start startet und eine REST API aufruft

        rest()
        .get("/callGet")
            .produces("text/plain")
            .to("log: Called Get!")
        .post("/callPost")
            .produces("text/plain")
            .to("log: Called Post!");

       from("jms:queue:CallPost")
           .id("examples.RestCallPost")
               .log("Message received from Artemis: ${body}")
           .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
               .setBody().simple("Hello to Rest!")
              .to("http://localhost:8080/callPost");

        from("jms:queue:CallGet")
            .id("examples.RestCallGet")
            .log("Message received from Artemis: ${body}")
            .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
                .to("http://localhost:8080/callGet");
    }
}
