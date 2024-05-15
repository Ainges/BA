package processors.PreOnboarding;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@ApplicationScoped
public class CalculateBrithdayProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(CalculateBrithdayProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {


        // first_working_day = "01.01.2025"
        // birth_date = "22.03.2000"
        // today = "26.04.2024"

        String message = exchange.getMessage().getBody(String.class);



        // Parse the message as json
        JsonReader jsonReader = Json.createReader(new StringReader(message));
        JsonObject jsonObject = jsonReader.readObject();

        // Get the birthdate from the json
        String first_working_day = jsonObject.getString("first_working_day");
        String birth_date = jsonObject.getString("birth_date");

        LocalDate first_working_day_date = LocalDate.parse(first_working_day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate birth_date_date = LocalDate.parse(birth_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // The Reason for the minusDays(5) is explained in the process.
        LocalDate today_with_buffer = LocalDate.now().minusDays(5);

        LocalDate nextBirthday = birth_date_date.withYear(today_with_buffer.getYear());
        JSONObject response = new JSONObject();

        if(nextBirthday.isBefore(first_working_day_date) && nextBirthday.isAfter(today_with_buffer)){
            logger.info("Next birthday is BEFORE first working day!");
             response.put("birthday", "before");
        }
        else if (nextBirthday.isEqual(first_working_day_date)){
            logger.info("Next birthday is ON the first working day!");
            response.put("birthday", "on");
        }
        else{
            logger.info("Next birthday is AFTER first working day!");
            response.put("birthday", "after");
        }

        exchange.getMessage().setHeader("Content-Type", "application/json");
        exchange.getMessage().setHeader("CamelHttpResponseCode", 200);
        exchange.getMessage().setBody(response.toString());

    }

}
