package processors.CanonicalDataModel;

import DTO.CanonicalUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Logger;

public class MapMessageToCanonicalUserDTOProcessor implements Processor {

    Logger logger = Logger.getLogger(MapMessageToCanonicalUserDTOProcessor.class.getName());

    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getMessage().getBody(String.class);

                    // map to CanonicalUserDTO
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        // print the body
                        logger.info("Body: " + body);
                        CanonicalUserDTO canonicalUserDTO = mapper.readValue(body, CanonicalUserDTO.class);
                        exchange.getMessage().setHeader("CanonicalUserDTO", canonicalUserDTO);
                    }
                    catch (Exception e) {

                        logger.info("Error while mapping to CanonicalUserDTO");
                        e.printStackTrace();
                    }
    }
}
