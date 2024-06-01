package processors.Onboarding;

import CDI.PlaceholderSubstitutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FirstPerformanceReviewResultsToNewEmployeeProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    @Inject
    PlaceholderSubstitutor placeholderSubstitutor;

    @ConfigProperty(name = "data.company.onboarding.email")
    String company_onboarding_email;

    Logger logger = LoggerFactory.getLogger(FirstPerformanceReviewResultsToNewEmployeeProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {

        String message = exchange.getMessage().getBody(String.class);

        try {
            // Parse the message as json
            JsonReader jsonReader = Json.createReader(new StringReader(message));
            JsonObject jsonObject = jsonReader.readObject();

            String companyEmailOfNewEmployee = jsonObject.getString("companyEmailOfNewEmployee");
            String goals = jsonObject.getString("goals");
            String trainings = jsonObject.getString("trainings");
            String currentTasks = jsonObject.getString("currentTasks");
            int satisfaction = jsonObject.getInt("satisfaction");
            String suggestions = jsonObject.getString("suggestions");

            String input = new String(Files.readAllBytes(Paths.get("src/main/resources/mailTemplates/FirstPerformanceReviewResults.html")));
            HashMap<String, String> valueMap = new HashMap<String, String>();

            valueMap.put("goals", goals);
            valueMap.put("trainings", trainings);
            valueMap.put("currentTasks", currentTasks);
            valueMap.put("satisfaction", String.valueOf(satisfaction));
            valueMap.put("suggestions", suggestions);

            String output = placeholderSubstitutor.substituteAll(input, valueMap);

            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("From", company_onboarding_email);
            headers.put("To", companyEmailOfNewEmployee);
            headers.put("Subject", "Die Ergebnisse des ersten Mitarbeitergesprächs");
            headers.put("Content-Type", "text/html; charset=utf-8");

            exchange.getMessage().setHeaders(headers);
            exchange.getMessage().setBody(output);

        } catch (Exception e) {

            logger.error("Error in FirstPerformanceReviewResultsToNewEmployeeProcessor: " + e.getMessage());



        }


    }
}
