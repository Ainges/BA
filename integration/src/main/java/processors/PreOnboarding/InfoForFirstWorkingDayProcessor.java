package processors.PreOnboarding;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class InfoForFirstWorkingDayProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    Logger logger = LoggerFactory.getLogger(InfoForFirstWorkingDayProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        // TODO implement the processing logic here

        // Replacement needed:
        // ${company_name}
        // ${last_name}
        // ${begin_of_first_working_day}
        // ${contact_person}
        // ${documents_needed_for_first_working_day}
        // ${company_address}

        // Get the message body
        String message = exchange.getMessage().getBody(String.class);

        logger.info("Got: " + message);





    }
}
