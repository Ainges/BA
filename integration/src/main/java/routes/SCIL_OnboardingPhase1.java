package routes;

import jakarta.inject.Inject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.HashMap;
import java.util.Map;

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
                .process(exchange -> {

                    Map<String, Object> headers = new HashMap<String, Object>();
                    headers.put("To", "test@thi.de");
                    headers.put("From", "hus4725@thi.de");
                    headers.put("Subject", "Grüße von Camel!");

                    String body = "Hallo THI.\nGreetings form a process driven Application!\n\nRegards Hubertus.";
                    exchange.getMessage().setHeaders(headers);
                    exchange.getMessage().setBody(body);


                })
                // SMTP Host and Port are set in the application.properties file
                // Note: It is not possible to use the @ConfigProperty annotation in the from() method -> will evaluate to null
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}");

    }
}
