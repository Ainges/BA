/*
package processors.SCIL_Preonboarding;

import DTO.SendWelcomeMessageToEmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Map;

public class SendWelcomeMessageToEmployeeProcessor implements Processor {

        @Override
        public void process(org.apache.camel.Exchange exchange) throws Exception {

            String inbody = exchange.getMessage().getBody().toString();
            ObjectMapper objectMapper = new ObjectMapper();
            SendWelcomeMessageToEmployeeDTO sendWelcomeMessageToEmployeeDTO = objectMapper.readValue(inbody, SendWelcomeMessageToEmployeeDTO.class);

            Map<String, Object> headers = new HashMap<String, Object>();
                    headers.put("To", sendWelcomeMessageToEmployeeDTO.getNewEmployeeData().getEmail());
                    headers.put("From", "onboarding@acme.de");
                    headers.put("Subject", "Wir freuen uns auf Sie!");

                    String body = "Hallo " + sendWelcomeMessageToEmployeeDTO.getNewEmployeeData().getfirst_name() + " " + sendWelcomeMessageToEmployeeDTO.getNewEmployeeData().getlast_name() + ",\n" +
                                  "Wir freuen uns, dass Sie ab dem " + sendWelcomeMessageToEmployeeDTO.getFirstWorkingDay()+ " bei uns anfangen.\n" +
                                  "Ihr Ansprechpartner ist " + sendWelcomeMessageToEmployeeDTO.getContactPerson() + ".\n" +
                                  "Bitte bringen Sie am ersten Arbeitstag folgendes mit: " + sendWelcomeMessageToEmployeeDTO.getDocumentsNeededForFirstWorkingDay() + ".\n" +
                                  "Ihr erster Arbeitstag beginnt um " + sendWelcomeMessageToEmployeeDTO.getBeginOfFirstWorkingDay() + ".\n\n" +
                                  "Viele Grüße\nIhr Onboarding Team";

                    exchange.getMessage().setHeaders(headers);
                    exchange.getMessage().setBody(body);
        }
}
*/
