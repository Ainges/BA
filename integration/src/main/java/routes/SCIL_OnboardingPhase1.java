package routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import processors.SendWelcomeMessageToEmployeeProcessor;

public class SCIL_OnboardingPhase1 extends RouteBuilder {

    @Override
    public void configure() throws Exception {



        rest("/onboarding/phase1")
                .post("/SendWelcomeMessageToNewEmployee")
                .consumes("application/json")
                .to("direct:SendWelcomeMessageToNewEmployee");

        from("direct:SendWelcomeMessageToNewEmployee")
                // Send to Artemis queue
                .log("Sending Welcome Message to new employee")
                .process(exchange -> {
                    String message = exchange.getMessage().getBody(String.class);
                    exchange.getMessage().setBody(message);
                })
                .to(ExchangePattern.InOnly, "jms:queue:SendWelcomeMessageToNewEmployee");


        from("jms:queue:SendWelcomeMessageToNewEmployee")
                .id("send-welcome-message-to-new-employee-with-E-MAIL-route")
                .log("Message received from Artemis: ${body}")
                .process(new SendWelcomeMessageToEmployeeProcessor())
                // SMTP Host and Port are set in the application.properties file
                // Note: It is not possible to use the @ConfigProperty annotation in the from() method -> will evaluate to null
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}");

    }
}
