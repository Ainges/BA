package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
                .unmarshal().json(JsonLibrary.Jackson, Map.class) // Konvertiert JSON in Map

                // either way - we have to return a password for the new user
            .choice()
                .when(simple("${body[isEmailAvailable]} == true"))
                    .log("Email is available")

                //.toD("http://{{engine.host}}:{{engine.port}}/atlas_engine/api/v1/messages/Message_Receive_MailisAvailable/trigger?bridgeEndpoint=true")
                .when(simple("${body[isEmailAvailable]} == false"))
                    .log("Email is not available")

                //.toD("http://{{engine.host}}:{{engine.port}}/atlas_engine/api/v1/messages/Message_Receive_MailisNotAvailable/trigger?bridgeEndpoint=true")
                .otherwise()
                // if something went wrong, let the request timeout and do the Task manually
                    .end();
        ;

    }
}
