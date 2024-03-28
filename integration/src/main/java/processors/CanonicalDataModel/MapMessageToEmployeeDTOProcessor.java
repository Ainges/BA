package processors.CanonicalDataModel;

import DTO.EmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Logger;

public class MapMessageToEmployeeDTOProcessor implements Processor {

    Logger logger = Logger.getLogger(MapMessageToEmployeeDTOProcessor.class.getName());

    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getMessage().getBody(String.class);

                    // map to EmployeeDTO
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        // print the body
                        EmployeeDTO employeeDTO = mapper.readValue(body, EmployeeDTO.class);
                        exchange.getMessage().setHeader("EmployeeDTO", employeeDTO);
                    }
                    catch (Exception e) {

                        logger.info("Error while mapping to EmployeeDTO");
                        e.printStackTrace();
                    }
    }
}
