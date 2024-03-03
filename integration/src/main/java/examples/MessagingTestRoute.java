package examples;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.ConfigProvider;

public class MessagingTestRoute extends RouteBuilder {
    /**
     * Configures the Camel routes for message processing.
     *
     * @throws Exception if an error occurs during configuration
     */

        String bearerToken = ConfigProvider.getConfig().getValue("engine.bearer", String.class);

    @Override
    public void configure() throws Exception {

        // Get the message from the queue of a Artemis broker

        from("jms:queue:exampleQueue")
                .id("artemis-consumer-route")
                .log("Message received from Artemis: ${body}")
                .to("log: LOG:");



        // ############### BPMN Message Test ################

//        from("timer:test?period=1000")
//                .setBody(constant("Test"))
//                .log("Sending message to BPMN")
//                .process(exchange -> {
//                    Message message = exchange.getMessage();
//                    //remove all headers to delete the query parameters
//                    message.removeHeaders("*");
//                    message.setHeader("Authorization", "Bearer " + bearerToken);
//                    message.setHeader("Content-Type", "application/json");
//                    message.setBody("{\n" +
//                                    "  \"testKey\": "testMessage"\n" +
//                                    "}");
//                })
//                .to("http://localhost:8000/atlas_engine/api/v1/messages/externalMessageTrigger/trigger?bridgeEndpoint=true");


    }
}
