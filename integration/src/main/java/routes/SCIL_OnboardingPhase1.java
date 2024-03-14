package routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.IsMovingNecessaryRequestProcessor;
import processors.OnTimePasswordGeneratorProcessor;
import processors.SendWelcomeMessageToEmployeeProcessor;
import processors.ValidateOneTimePasswordProcessor;

public class SCIL_OnboardingPhase1 extends RouteBuilder {

    String bearerToken = ConfigProvider.getConfig().getValue("engine.bearer", String.class);

    Logger logger = LoggerFactory.getLogger(SCIL_OnboardingPhase1.class);

    @Override
    public void configure() throws Exception {


        rest("/onboarding/phase1")
                .post("/SendWelcomeMessageToNewEmployee")
                .consumes("application/json")
                .to("direct:SendWelcomeMessageToNewEmployee")
                .post("/MovingRequest")
                .consumes("application/json")
                .to("direct:MovingRequest")

                .get("/MovingRequest/accept")
                .produces("text/html")
                .to("direct:MovingRequestAccept")
                .get("/MovingRequest/decline")
                .produces("text/html")
                .to("direct:MovingRequestDecline");

        // ############### Welcome Message ################

        from("direct:SendWelcomeMessageToNewEmployee")
                // Send to Artemis queue
                .log("Sending Welcome Message to new employee")
                .process(exchange -> {
                    String message = exchange.getMessage().getBody(String.class);
                    exchange.getMessage().setBody(message);
                })
                .to(ExchangePattern.InOnly, "jms:queue:SendWelcomeMessageToNewEmployeeQueue");


        from("jms:queue:SendWelcomeMessageToNewEmployeeQueue")
                .id("send-welcome-message-to-new-employee-with-E-MAIL-route")
                .log("Message received from Artemis: ${body}")
                .process(new SendWelcomeMessageToEmployeeProcessor())
                // SMTP Host and Port are set in the application.properties file
                // Note: It is not possible to use the @ConfigProperty annotation in the from() method -> will evaluate to null
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}");


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
                .process(new OnTimePasswordGeneratorProcessor())
                .log("Generating email with one-time password...")
                .process(new IsMovingNecessaryRequestProcessor())
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}");


        // ############### Moving Request Accept ################

        from("direct:MovingRequestAccept")
                .id("moving-request-accept-produces-jms-queue-route")
                .log("Acceptence of moving request called!")
                .setHeader("answerOfNewEmployee", constant("accepted"))
                .process(new ValidateOneTimePasswordProcessor())
                //TODO: inform the process of the Acceptance
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
                .toD("http://localhost:8000/atlas_engine/api/v1/messages/Message_ResponseForMove/trigger?bridgeEndpoint=true")
                .log("Process informed!");


        // ############### Moving Request Decline ################

        from("direct:MovingRequestDecline")
                .id("moving-request-decline-produces-jms-queue-route")
                .log("Decline of moving request called!")
                .setHeader("answerOfNewEmployee", constant("declined"))
                .process(new ValidateOneTimePasswordProcessor())
                //TODO: inform the process of the Decline
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

                .to("http://localhost:8000/atlas_engine/api/v1/messages/Message_ResponseForMove/trigger?bridgeEndpoint=true")
        .log("Process informed!");



    }
}
