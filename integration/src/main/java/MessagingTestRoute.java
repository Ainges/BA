import org.apache.camel.builder.RouteBuilder;

public class MessagingTestRoute extends RouteBuilder {
    /**
     * Configures the Camel routes for message processing.
     *
     * @throws Exception if an error occurs during configuration
     */
    @Override
    public void configure() throws Exception {

        // Get the message from the queue of a Artemis broker

        from("jms:queue:exampleQueue")
                .id("artemis-consumer-route")
                .log("Message received from Artemis: ${body}")
                .to("log: LOG:");

        // Build new Route for another Queue
        

    }
}
