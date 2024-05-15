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
public class InfoForFirstWorkingDayProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    Logger logger = LoggerFactory.getLogger(InfoForFirstWorkingDayProcessor.class);

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
        // TODO implement the processing logic here

        // Replacement needed:
        // ${company_name}
        // ${last_name} -> message
        // ${begin_of_first_working_day} -> message
        // ${contact_person} -> message
        // ${documents_needed_for_first_working_day} -> message
        // ${company_address}
        // ${first_working_day} -> message

        // Get the message body
        String message = exchange.getMessage().getBody(String.class);
        logger.info("Got in InfoForFirstWorkingDayProcessor: " + message);

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
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // final date String
        String first_working_day = first_working_day_asDate.format(outputFormatter);


        String input = new String(Files.readAllBytes(Paths.get("src/main/resources/mailTemplates/InfoForFirstWorkingDay.html")));
        HashMap<String, String> valueMap = new HashMap<String, String>();

        // Data from application.properties
        valueMap.put("company_name", company_name);
        valueMap.put("company_address", company_address);

        // Data from message
        valueMap.put("last_name", last_name);
        valueMap.put("begin_of_first_working_day", begin_of_first_working_day);
        valueMap.put("contact_person", contact_person);
        // TODO: Allow multiple documents
        valueMap.put("documents_needed_for_first_working_day", "<li>" + documents_needed_for_first_working_day + "</li>");
        valueMap.put("first_working_day", first_working_day);
        valueMap.put("contact_person_mail", contact_person_mail);


        String output = placeholderSubstitutor.substituteAll(input, valueMap);


        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("From", company_onboarding_email);
        headers.put("To", private_email);
        headers.put("Subject", "Informationen für Ihren ersten Arbeitstag |" + company_name);
        headers.put("Content-Type", "text/html; charset=utf-8");

        exchange.getMessage().setHeaders(headers);
        exchange.getMessage().setBody(output);


    }
}
