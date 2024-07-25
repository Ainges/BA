package processors.PreOnboarding;

import CDI.OneTimePasswordGenerator;
import DTO.IsMovingRequestNecessaryDTO;
import Entities.OneTimePasswordEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OneTimePasswordEntityRepository;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Generates and stores a one-time password.
 * This is usefully for sending a one-time password to a new employee, who has not yet received his/her credentials.
 *
 * IN:
 *    Body: IsMovingRequestNecessaryDTO
 *    Header: keine
 *
 * OUT:
 *   Body: IsMovingRequestNecessaryDTO
 *   Header: one_time_password
 *
 */
@ApplicationScoped
public class OnTimePasswordGeneratorProcessor implements Processor {


    @Inject
    OneTimePasswordEntityRepository oneTimePasswordEntityRepository;

    @Inject
    OneTimePasswordGenerator oneTimePasswordGenerator;

    Logger logger = LoggerFactory.getLogger(OnTimePasswordGeneratorProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        // Generate one_time_password and store it in header
        int lengthOfOneTimePassword = 5;
        String one_time_password = oneTimePasswordGenerator.generateOneTimePassword(lengthOfOneTimePassword);
        logger.info("One-time password generated: " + one_time_password);
        exchange.getMessage().setHeader("one_time_password", one_time_password);

        // Get the body of the message
        String body = exchange.getMessage().getBody().toString();
        IsMovingRequestNecessaryDTO isMovingRequestNecessaryDTO = new ObjectMapper().readValue(body, IsMovingRequestNecessaryDTO.class);

        // Insert one-time password into database
        logger.info("Inserting one-time password into database...");
        insertOneTimePasswordIntoDatabase(one_time_password, isMovingRequestNecessaryDTO.getFirst_name(), isMovingRequestNecessaryDTO.getLast_name());
        logger.info("One-time password successfully inserted into database.");


    }



    @Transactional
    public void insertOneTimePasswordIntoDatabase(String one_time_password, String first_name, String last_name) {

        OneTimePasswordEntity oneTimePasswordEntity = new OneTimePasswordEntity(one_time_password, first_name, last_name);
        // persist the one-time password
        oneTimePasswordEntityRepository.persist(oneTimePasswordEntity);

    }

}
