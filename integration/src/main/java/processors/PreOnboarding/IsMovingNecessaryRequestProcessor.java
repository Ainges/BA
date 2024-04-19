package processors.PreOnboarding;

import CDI.PlaceholderSubstitutor;
import DTO.IsMovingRequestNecessaryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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


/**
 *
 * IN:
 *      Body: IsMovingRequestNecessaryDTO
 *      Header: one_time_password
 *
 * Out:
 *      Body: HTML email
 *      Header: From, To, Subject, Content-Type
 *
 * */
@ApplicationScoped
public class IsMovingNecessaryRequestProcessor implements Processor {

    @ConfigProperty(name = "camel.host")
    String camelHost;

    @ConfigProperty(name = "camel.port")
    String camelPort;

    @ConfigProperty(name = "data.company.onboarding.email")
    String company_onboarding_email;

    @ConfigProperty(name = "data.company.name")
    String company_name;

    Logger logger = LoggerFactory.getLogger(IsMovingNecessaryRequestProcessor.class);

    @Inject
    PlaceholderSubstitutor placeholderSubstitutor;

    @Override
    public void process(Exchange exchange) throws Exception {

        String inbody = exchange.getMessage().getBody().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        IsMovingRequestNecessaryDTO isMovingRequestNecessaryDTO = objectMapper.readValue(inbody, IsMovingRequestNecessaryDTO.class);

        // get information needed for the email

        String one_time_password = exchange.getMessage().getHeader("one_time_password").toString();
        String first_name = isMovingRequestNecessaryDTO.getFirst_name();
        String last_name = isMovingRequestNecessaryDTO.getLast_name();


        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("From", company_onboarding_email);
        headers.put("To", isMovingRequestNecessaryDTO.getEmail());
        headers.put("Subject", "Steht bei Ihnen ein Umzug an? |" + company_name);
        headers.put("Content-Type", "text/html; charset=utf-8");
        String input = new String(Files.readAllBytes(Paths.get("src/main/resources/mailTemplates/MovingRequest.html")));

        HashMap<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("first_name", first_name);
        valuesMap.put("last_name", last_name);
        valuesMap.put("one_time_password", one_time_password);
        valuesMap.put("camel_host", camelHost);
        valuesMap.put("camel_port", camelPort);

        String output = placeholderSubstitutor.substituteAll(input, valuesMap);

        // Set the headers and the body of the exchange
        exchange.getMessage().setHeaders(headers);
        exchange.getMessage().setBody(output);


    }
}
