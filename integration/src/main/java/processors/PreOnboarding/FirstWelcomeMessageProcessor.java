package processors.PreOnboarding;

import jakarta.activation.FileDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;


@ApplicationScoped
public class FirstWelcomeMessageProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    @Inject
    FirstWelcomeMessageProcessor firstWelcomeMessageProcessor;

    @ConfigProperty(name = "data.company.name")
    String companyName;

    @Override
    public void process(Exchange exchange) throws Exception {

        String input = new FileDataSource("src/main/resources/mailTemplates/welcomeMessage.html").getInputStream().toString();

        String employeeName = exchange.getMessage().getHeader("first_name").toString()+" "+exchange.getMessage().getHeader("last_name").toString();

        // ***** suggested by ChatGPT *****
        // Erstellen Sie eine Map mit den Platzhaltern und ihren Werten
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("{{companyName}}", companyName);
        valuesMap.put("{{employeeName}}", "");

        // Erstellen Sie einen StringSubstitutor mit den Platzhaltern und ihren Werten
        StringSubstitutor substitutor = new StringSubstitutor(valuesMap);

        // Ersetzen Sie die Platzhalter im Eingabe-String
        String output = substitutor.replace(input);

        // ********************************
        exchange.getMessage().setBody(output);

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("From", "onboarding@acme.de");
        headers.put("To", exchange.getMessage().getHeader("email"));
        headers.put("Subject", "Willkommen bei " + companyName + "!");
        headers.put("Content-Type", "text/html; charset=utf-8");


    }
}
