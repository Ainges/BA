package processors;

import DTO.NewEmployeeDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Map;

public class IsMovingNecessaryRequestProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        String inbody = exchange.getMessage().getBody().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        NewEmployeeDataDTO newEmployeeDataDTO = objectMapper.readValue(inbody, NewEmployeeDataDTO.class);

        String oneTimePassword = exchange.getMessage().getHeader("OneTimePassword").toString();


        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("From", "onboarding@acme.de");
        headers.put("To", newEmployeeDataDTO.getEmail());
        headers.put("Subject", "Steht bei Ihnen ein Umzug an?");
        // encoding is set to utf-8 and content type to html
        headers.put("Content-Type", "text/html; charset=utf-8");
        String body = "<html><body><p>Hallo " + "Herr/Frau " + newEmployeeDataDTO.getLastName() + ",</p>" +
                      "<p>herzlichen Glückwunsch zu Ihrem neuen Arbeitsplatz!. Wir freuen uns, dass Sie sich für uns entschieden haben.</p>" +
                      "<p>Steht bei Ihnen ein Umzug an? Falls ja, würden wir Ihnen gerne dabei behilflich sein. Bitte teilen Sie uns mit, ob Sie Unterstützung benötigen.</p>" +
                      "<p>Wir freuen uns auf Ihre Rückmeldung.</p>" +
                      // Add two links either to accept or decline the moving request
                      "<p>Bitte klicken Sie auf einen der folgenden Links, um uns mitzuteilen, ob Sie Hilfe bei Ihrem Umzug benötigen:</p>" +
                      "<p> Ihr einmaliges Passwort lautet: " + exchange.getMessage().getHeader("OneTimePassword").toString() + "</p>"+
                      "<p>(Sie benötigen das Passwort nur, wenn die Links nicht funktionieren sollten)"+"</p>"+
                      "<a href=\"http://localhost:8080/onboarding/phase1/MovingRequest/accept?oneTimePassword="+exchange.getMessage().getHeader("OneTimePassword").toString()+
                      "\">Hilfe bei Umzug gewünscht</a><br>" +
                      "<a href=\"http://localhost:8080/onboarding/phase1/MovingRequest/decline?oneTimePassword="+exchange.getMessage().getHeader("OneTimePassword").toString() +
                      "\">Es steht kein Umzug an, Hilfe wird nicht gewünscht</a>" +
                      "<br>" +
                      "<p>Viele Grüße</p><p>Ihr Onboarding Team</p></body></html>";

                    exchange.getMessage().setHeaders(headers);
                    exchange.getMessage().setBody(body);


    }
}
