package processors.PreOnboarding;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@ApplicationScoped
public class InformNewUserAboutAccountProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(InformNewUserAboutAccountProcessor.class);

    @Inject
    PlaceholderSubstitutor placeholderSubstitutor;

    @ConfigProperty(name = "data.company.name")
    String company_name;

    @ConfigProperty(name = "data.company.onboarding.email")
    String company_onboarding_email;

    @ConfigProperty(name = "data.company.portal.url")
    String company_portal_url;

    @Override
    public void process(Exchange exchange) throws Exception {

        String message = exchange.getMessage().getBody(String.class);

        logger.info("Trying to extract data from message: " + message);

        String email = "";
        String password = "";
        String first_name = "";
        String last_name = "";
        String private_email = "";
        String first_working_day = "";

        // Extraction of data from message
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(message));
            JsonObject jsonObject = jsonReader.readObject();

            email = jsonObject.getString("email");
            password = jsonObject.getString("password");

            JsonObject onboardingData = jsonObject.getJsonObject("OnboardingData");
            first_name = onboardingData.getString("first_name");
            last_name = onboardingData.getString("last_name");
            private_email = onboardingData.getString("private_email");

            String raw_first_working_day = onboardingData.getString("first_working_day");
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate first_working_day_asDate = LocalDate.parse(raw_first_working_day, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            first_working_day = first_working_day_asDate.format(outputFormatter);

            if(private_email.isEmpty()){
                throw new Exception("Private Email is empty");
            }


        } catch (Exception e) {
            logger.error("Error while extracting data from message: " + e.getMessage());
        }

        // Prepare the email
        HashMap<String, String> ValueMap = new HashMap<>();
        ValueMap.put("email", email);
        ValueMap.put("password", password);
        ValueMap.put("first_name", first_name);
        ValueMap.put("last_name", last_name);
        ValueMap.put("private_email", private_email);
        ValueMap.put("first_working_day", first_working_day);
        ValueMap.put("company_name", company_name);
        ValueMap.put("company_portal_url", company_portal_url);

        String input = new String(Files.readAllBytes(Paths.get("src/main/resources/mailTemplates/InformNewUserAboutAccount.html")));
        String output = placeholderSubstitutor.substituteAll(input, ValueMap);


        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("From", company_onboarding_email);
        headers.put("To", private_email);
        headers.put("Subject", "Ihr Equipment & Mitarbeiter-Zugang! |" + company_name);
        headers.put("Content-Type", "text/html; charset=utf-8");

        exchange.getMessage().setHeaders(headers);
        exchange.getMessage().setBody(output);


    }
}