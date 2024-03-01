package routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import processors.IsMovingNecessaryRequestProcessor;
import processors.OnTimePasswordGenerator;
import processors.SendWelcomeMessageToEmployeeProcessor;
import processors.ValidateOneTimePasswordProcessor;

public class SCIL_OnboardingPhase1 extends RouteBuilder {

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
                .process(new OnTimePasswordGenerator())
                .log("Generating email with one-time password...")
                .process(new IsMovingNecessaryRequestProcessor())
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}");


        // ############### Moving Request Accept ################

        from("direct:MovingRequestAccept")
                .id("moving-request-accept-to-jms-route")
                .log("Acceptence of moving request called!")
                .setHeader("answerOfNewEmployee", constant("accepted"))
                .process(new ValidateOneTimePasswordProcessor())
                //TODO: inform the process of the Acceptance
                        .end();



        // ############### Moving Request Decline ################

        from("direct:MovingRequestDecline")
                .id("moving-request-decline-route")
                .log("Decline of moving request called!")
                .setHeader("answerOfNewEmployee", constant("declined"))
                .process(new ValidateOneTimePasswordProcessor())
                //TODO: inform the process of the Decline
                        .end();

        }
}
