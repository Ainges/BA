package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.PreOnboarding.FirstWelcomeMessageProcessor;

import java.io.StringReader;

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

    Logger logger = LoggerFactory.getLogger(PreOnboardingFirstWelcomeMessageRoute.class);


    @Override
    public void configure() throws Exception {

        // ############### Welcome Message ################

        from("direct:FirstWelcomeMessageToBroker")
                // Send to Artemis queue
                .log("Sending Welcome Message to new employee")
                .id("send-welcome-message-to-new-employee-with-Artemis-route")
                .to(ExchangePattern.InOnly, "jms:queue:SendWelcomeMessageToNewEmployeeQueue");


        from("jms:queue:SendWelcomeMessageToNewEmployeeQueue")
                .id("send-welcome-message-to-new-employee-with-E-MAIL-route")
                .log("Message received from Artemis: ${body}")
                .process(exchange -> {

                    // TODO: use Body instead of Headers
                    String message = exchange.getMessage().getBody(String.class);
                    // get the first name and last name and email from the message
                    String firstName = Json.createReader(new StringReader(message)).readObject().getString("first_name");
                    String lastName = Json.createReader(new StringReader(message)).readObject().getString("last_name");
                    String email = Json.createReader(new StringReader(message)).readObject().getString("email");
                    // set the headers
                    logger.info("Trying to extract headers from the message...");
                    exchange.getMessage().setHeader("first_name", firstName);
                    exchange.getMessage().setHeader("last_name", lastName);
                    exchange.getMessage().setHeader("email", email);
                    logger.info("Headers extracted successfully!");
                })
                .process(firstWelcomeMessageProcessor)
                .to("smtp://" + smtpHost + ":" + smtpPort + "?username=" + smtpUsername + "&password=" + smtpPassword);

    }
}
