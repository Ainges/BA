package processors.PreOnboarding;

import CDI.PlaceholderSubstitutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@ApplicationScoped
public class FirstWelcomeMessageProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    @Inject
    PlaceholderSubstitutor placeholderSubstitutor;

    @ConfigProperty(name = "data.company.name")
    String companyName;

    @ConfigProperty(name = "data.company.onboarding.email")
    String company_onboarding_email;

    Logger logger = LoggerFactory.getLogger(FirstWelcomeMessageProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        String input = new String(Files.readAllBytes(Paths.get("src/main/resources/mailTemplates/welcomeMessage.html")));

        String employeeName = exchange.getMessage().getHeader("first_name").toString() + " " + exchange.getMessage().getHeader("last_name").toString();

        // ***** suggested by ChatGPT *****
        // Erstellen Sie eine Map mit den Platzhaltern und ihren Werten
        HashMap<String, String> valuesMap = new HashMap<>();
        valuesMap.put("companyName", companyName);
        valuesMap.put("employeeName", employeeName);

        String output = placeholderSubstitutor.substituteAll(input, valuesMap);

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("From", company_onboarding_email);
        headers.put("To", exchange.getMessage().getHeader("email"));
        headers.put("Subject", "Willkommen bei " + companyName + "!");
        headers.put("Content-Type", "text/html; charset=utf-8");

        exchange.getMessage().setHeaders(headers);
        exchange.getMessage().setBody(output);


    }
}
