package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.PreOnboarding.IsMovingNecessaryRequestProcessor;
import processors.PreOnboarding.OnTimePasswordGeneratorProcessor;
import processors.PreOnboarding.ValidateOneTimePasswordProcessor;

@ApplicationScoped
public class PreOnboardingMovingRequestRoute extends RouteBuilder {

    @Inject
    IsMovingNecessaryRequestProcessor isMovingNecessaryRequestProcessor;

    @Inject
    OnTimePasswordGeneratorProcessor onTimePasswordGeneratorProcessor;

    @Inject
    ValidateOneTimePasswordProcessor validateOneTimePasswordProcessor;


    String bearerToken = ConfigProvider.getConfig().getValue("engine.bearer", String.class);

    Logger logger = LoggerFactory.getLogger(PreOnboardingMovingRequestRoute.class);

    @Override
    public void configure() throws Exception {

        // ############### Moving Request ################

        from("direct:MovingRequest")
                // Send to Artemis queue
                .log("Sending \"MovingRequest\" to Artemis")
                .process(exchange -> {
                    String message = exchange.getMessage().getBody(String.class);
                    exchange.getMessage().setBody(message);
                })
                .to(ExchangePattern.InOnly, "jms:queue:MovingRequest");

        from("jms:queue:MovingRequest")
                .id("moving-request-route")
                // Bearbeite Anfrage zur Umzugsnotwendigkeit in Englisch
                .log("Processing \"MovingRequest\" from Artemis: ${body}")
                .log("Generating one-time password...")
                .process(onTimePasswordGeneratorProcessor)
                .log("Generating email with one-time password...")
                .process(isMovingNecessaryRequestProcessor)
                .log("Sending email to new employee...")
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}")
                .log("Email successfully sent to new employee!");


        // ############### Moving Request Accept ################

        from("direct:MovingRequestAccept")
                .id("moving-request-accept-produces-jms-queue-route")
                .log("Acceptence of moving request called!")
                .setHeader("answerOfNewEmployee", constant("accepted"))
                .process(validateOneTimePasswordProcessor)
                .to(ExchangePattern.InOnly, "jms:queue:MovingRequestAccept");

        from("jms:queue:MovingRequestAccept")
                .id("moving-request-accept-consume-jms-queue-route")
                .log("Informing the process of the Acceptance of the moving request")
                .process(exchange -> {

                    Message message = exchange.getMessage();
                    //remove all headers to delete the query parameters
                    message.removeHeaders("*");
                    message.setHeader("Authorization", "Bearer " + bearerToken);
                    message.setHeader("Content-Type", "application/json");
                    message.setBody("{\n" +
                                    "  \"help_wanted\": true\n" +
                                    "}");

                })
                .toD("http://{{engine.host}}:{{engine.port}}/atlas_engine/api/v1/messages/Message_ResponseForMove/trigger?bridgeEndpoint=true")
                .log("Process informed!");


        // ############### Moving Request Decline ################

        from("direct:MovingRequestDecline")
                .id("moving-request-decline-produces-jms-queue-route")
                .log("Decline of moving request called!")
                .setHeader("answerOfNewEmployee", constant("declined"))
                .process(validateOneTimePasswordProcessor)
                .to(ExchangePattern.InOnly, "jms:queue:MovingRequestDecline");

        from("jms:queue:MovingRequestDecline")
                .id("moving-request-decline-consume-jms-queue-route")
                .log("Informing the process of the Decline of the moving request")
                .process(exchange -> {

                    Message message = exchange.getMessage();
                    //remove all headers to delete the query parameters
                    message.removeHeaders("*");
                    message.setHeader("Authorization", "Bearer " + bearerToken);
                    message.setHeader("Content-Type", "application/json");
                    message.setBody("{\n" +
                                    "  \"help_wanted\": false\n" +
                                    "}");

                })

                .to("http://{{engine.host}}:{{engine.port}}/atlas_engine/api/v1/messages/Message_ResponseForMove/trigger?bridgeEndpoint=true")
                .log("Process informed!");


    }
}
