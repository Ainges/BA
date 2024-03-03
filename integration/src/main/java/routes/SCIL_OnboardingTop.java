package routes;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.PropertiesComponent;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class SCIL_OnboardingTop extends RouteBuilder {

    // Bearer token for the engine
    // TODO: Check why {{engine.bearer}} is not working
    String bearerToken = ConfigProvider.getConfig().getValue("engine.bearer", String.class);

    @Override
    public void configure() throws Exception {

        rest("/onboarding/top")
                .post("/employeeHasSignedContract")
                .consumes("application/json")
                .to("direct:employeeHasSignedContract");


        from("direct:employeeHasSignedContract")
                .id("Employee-has-signed-contract-Route-to-Broker")
                .log("New Employee has signed a contract!")
                .log("\n${body}")
                .to("jms:queue:employeeHasSignedContract");


        from("jms:queue:employeeHasSignedContract")
                .id("Employee-has-signed-contract-Route-message-trigger-on-Engine")
                .log("Triggering a new onboarding Process...")
                .process(exchange -> {

                    Message message = exchange.getMessage();
                    message.setHeader("Authorization", "Bearer " + bearerToken);
                })
                // BridgeEndpoint is set to true because this option tells Camel that it should ignore the URI in the incoming message and use the URI configured on the endpoint instead.
                .toD("http://localhost:8000/atlas_engine/api/v1/messages/employeeHasSignedContract/trigger?bridgeEndpoint=true");


    }
}
