package routes.preonboarding;

import CDI.OneTimePasswordGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import processors.CanonicalDataModel.CheckMailAvailabilityProcessor;
import processors.PreOnboarding.CraftEmailFromNameProcessor;

import java.util.Map;

@ApplicationScoped
public class PreOnboardingTryAutoUserCreationRoute extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */

    @Inject
    CheckMailAvailabilityProcessor checkEmailAvailabilityProcessor;

    @Inject
    CraftEmailFromNameProcessor craftEmailFromNameProcessor;

    @Inject
    OneTimePasswordGenerator oneTimePasswordGenerator;

    @ConfigProperty(name = "engine.bearer")
    String bearerToken;

    @Override
    public void configure() throws Exception {

        from("direct:TryAutoUserCreationToBroker")
                .id("try-Auto-User-Creation-Route-To-Broker")
                .log("TryAutoUserCreation: Trying to create user...")
                .to("jms:queue:TryAutoUserCreation");

        from("jms:queue:TryAutoUserCreation")
                .id("try-Auto-User-Creation-Route-From-Broker")
                // extract email from request to header "email"
                .log("Got message from Broker: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, Map.class) // Konvertiert JSON in Map
                .process(craftEmailFromNameProcessor)
                .log("Checking email availability... (${header.email})")
                // Body is overwritten with the result of the check
                .process(checkEmailAvailabilityProcessor)
                .log("Email availability checked. ${body}")
                // either way - we have to return a password for the new user
                .unmarshal().json(JsonLibrary.Jackson, Map.class) // Konvertiert JSON in Map

                .choice()
                .when(simple("${body[isEmailAvailable]} == true"))
                    .log("Email is available")
                    // add "email" and "password" to the body
                    .process(exchange -> {
                         String email = exchange.getMessage().getHeader("email", String.class);

                        Message message = exchange.getMessage();
                        message.removeHeaders("*");
                        message.setHeader("Authorization", "Bearer " + bearerToken);
                        message.setHeader("Content-Type", "application/json");

                        String password = oneTimePasswordGenerator.generateOneTimePassword(8);
                        Map<String, Object> body = exchange.getMessage().getBody(Map.class);

                        body.put("password", password);
                        body.put("email", email);
                    })
                    .marshal().json(JsonLibrary.Jackson)
                    .log("Responding Engine: ${body}")
//                    .toD("http://{{engine.host}}:{{engine.port}}/atlas_engine/api/v1/messages/Message_MailisAvailable/trigger?bridgeEndpoint=true")

                .when(simple("${body[isEmailAvailable]} == false"))
                    .log("Email is not available")
                    // so only return the password with the old data
                    .process(exchange -> {

                        Message message = exchange.getMessage();
                        message.removeHeaders("*");
                        message.setHeader("Authorization", "Bearer " + bearerToken);
                        message.setHeader("Content-Type", "application/json");

                        String password = oneTimePasswordGenerator.generateOneTimePassword(8);
                        Map<String, Object> body = exchange.getMessage().getBody(Map.class);
                        String email = exchange.getMessage().getHeader("email", String.class);
                        body.put("password", password);
                    })
                    .marshal().json(JsonLibrary.Jackson)
//                    .toD("http://{{engine.host}}:{{engine.port}}/atlas_engine/api/v1/messages/Message_MailisNotAvailable/trigger?bridgeEndpoint=true")

                .end();
        ;

    }
}
