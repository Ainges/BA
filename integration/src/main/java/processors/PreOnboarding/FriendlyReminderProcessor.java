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

/**
 * IN:
 * Body: lastname, begin_of_first_working_day, contact_person, documents_needed_for_first_working_day, private_email, first_working_day, contact_person_mail
 * <p>
 * Out:
 * Body: email
 * Header: From, To, Subject, Content-Type
 */

@ApplicationScoped
public class FriendlyReminderProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    Logger logger = LoggerFactory.getLogger(FriendlyReminderProcessor.class);

    @ConfigProperty(name = "data.company.name")
    String company_name;

    @ConfigProperty(name = "data.company.address")
    String company_address;

    @ConfigProperty(name = "data.company.onboarding.email")
    String company_onboarding_email;

    @Inject
    PlaceholderSubstitutor placeholderSubstitutor;

    @Override
    public void process(Exchange exchange) throws Exception {

        String message = exchange.getMessage().getBody(String.class);
        logger.info("Got in FriendlyReminderProcessor: " + message);

        // Parse the message as json
        JsonReader jsonReader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = jsonReader.readObject();

        // Extract Data form Message
        String last_name = jsonObject.getString("last_name");
        String begin_of_first_working_day = jsonObject.getString("begin_of_first_working_day");
        String contact_person = jsonObject.getString("contact_person");
        String documents_needed_for_first_working_day = jsonObject.getString("documents_needed_for_first_working_day");
        String private_email = jsonObject.getString("private_email");
        String raw_first_working_day = jsonObject.getString("first_working_day");
        String contact_person_mail = jsonObject.getString("contact_person_mail");

        // Format the date
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate first_working_day_asDate = LocalDate.parse(raw_first_working_day, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        // final date String
        String first_working_day = first_working_day_asDate.format(outputFormatter);

        HashMap<String, String> ValueMap = new HashMap<>();

        ValueMap.put("last_name", last_name);
        ValueMap.put("begin_of_first_working_day", begin_of_first_working_day);
        ValueMap.put("contact_person", contact_person);
        ValueMap.put("documents_needed_for_first_working_day", "<li>"+documents_needed_for_first_working_day+"</li>");
        ValueMap.put("private_email", private_email);
        ValueMap.put("first_working_day", first_working_day);
        ValueMap.put("contact_person_mail", contact_person_mail);
        ValueMap.put("company_name", company_name);
        ValueMap.put("company_address", company_address);

        String input = new String(Files.readAllBytes(Paths.get("src/main/resources/mailTemplates/FriendlyReminder.html")));

        String output = placeholderSubstitutor.substituteAll(input, ValueMap);

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("From", company_onboarding_email);
        headers.put("To", private_email);
        headers.put("Subject", "Bald geht es los! |" + company_name);
        headers.put("Content-Type", "text/html; charset=utf-8");

        exchange.getMessage().setHeaders(headers);
        exchange.getMessage().setBody(output);


    }
}
