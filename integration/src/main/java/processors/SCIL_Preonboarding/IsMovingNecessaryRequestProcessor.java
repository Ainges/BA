package processors.SCIL_Preonboarding;

import DTO.IsMovingRequestNecessaryDTO;
import Entities.OneTimePasswordEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

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
        headers.put("From", "onboarding@acme.de");
        headers.put("To", isMovingRequestNecessaryDTO.getEmail());
        headers.put("Subject", "Steht bei Ihnen ein Umzug an?");
        // encoding is set to utf-8 and content type to html
        headers.put("Content-Type", "text/html; charset=utf-8");
        String body = "<html><body><p>Hallo " + "Herr/Frau " + isMovingRequestNecessaryDTO.getLast_name() + ",</p>" +
                      "<p>herzlichen Glückwunsch zu Ihrem neuen Arbeitsplatz!. Wir freuen uns, dass Sie sich für uns entschieden haben.</p>" +
                      "<p>Steht bei Ihnen ein Umzug an? Falls ja, würden wir Ihnen gerne dabei behilflich sein. Bitte teilen Sie uns mit, ob Sie Unterstützung benötigen.</p>" +
                      "<p>Wir freuen uns auf Ihre Rückmeldung.</p>" +
                      // Add two links either to accept or decline the moving request
                      "<p>Bitte klicken Sie auf einen der folgenden Links, um uns mitzuteilen, ob Sie Hilfe bei Ihrem Umzug benötigen:</p>" +
                      "<p> Ihr einmaliges Passwort lautet: " + one_time_password + "</p>"+
                      "<p>(Sie benötigen das Passwort nur, wenn die Links nicht funktionieren sollten)"+"</p>"+
                      "<a href=\"http://localhost:8080/onboarding/preonboarding/MovingRequest/accept"+
                      "?one_time_password="+one_time_password+"&"+
                        "first_name="+first_name+"&"+
                        "last_name="+last_name+
                      "\">Hilfe bei Umzug gewünscht</a><br>" +
                      "<a href=\"http://localhost:8080/onboarding/preonboarding/MovingRequest/decline"+
                      "?one_time_password="+one_time_password+"&"+
                        "first_name="+first_name+"&"+
                        "last_name="+last_name+
                      "\">Es steht kein Umzug an, Hilfe wird nicht gewünscht</a>" +
                      "<br>" +
                      "<p>Viele Grüße</p><p>Ihr Onboarding Team</p></body></html>";

                    exchange.getMessage().setHeaders(headers);
                    exchange.getMessage().setBody(body);


    }
}
