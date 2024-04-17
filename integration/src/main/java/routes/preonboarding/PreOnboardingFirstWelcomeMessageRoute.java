package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import processors.PreOnboarding.FirstWelcomeMessageProcessor;

@ApplicationScoped
public class PreOnboardingFirstWelcomeMessageRoute extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */


    @Inject
    FirstWelcomeMessageProcessor firstWelcomeMessageProcessor;

    @ConfigProperty(name = "smtp.host")
    String smtpHost;

    @ConfigProperty(name = "smtp.port")
    String smtpPort;

    @ConfigProperty(name = "smtp.username")
    String smtpUsername;

    @ConfigProperty(name = "smtp.password")
    String smtpPassword;

    @Override
    public void configure() throws Exception {

        // ############### Welcome Message ################

        from("direct:FirstWelcomeMessageToBroker")
                // Send to Artemis queue
                .log("Sending Welcome Message to new employee")

                //TODO: Necessary?
                .process(exchange -> {
                    String message = exchange.getMessage().getBody(String.class);
                    exchange.getMessage().setBody(message);
                })
                .to(ExchangePattern.InOnly, "jms:queue:SendWelcomeMessageToNewEmployeeQueue");


        from("jms:queue:SendWelcomeMessageToNewEmployeeQueue")
                .id("send-welcome-message-to-new-employee-with-E-MAIL-route")
                .log("Message received from Artemis: ${body}")
                .process(firstWelcomeMessageProcessor)
                .to("smtp://" + smtpHost + ":" + smtpPort + "?username=" + smtpUsername + "&password=" + smtpPassword);

    }
}
