package processors.SCIL_Preonboarding;

import DTO.EmployeeDTO;
import DTO.IsMovingRequestNecessaryDTO;
import Entities.OneTimePasswordEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OneTimePasswordEntityRepository;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    Logger logger = LoggerFactory.getLogger(OnTimePasswordGeneratorProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        // Generate one_time_password and store it in header
        int lengthOfOneTimePassword = 5;
        String one_time_password = generateOneTimePassword(lengthOfOneTimePassword);
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

    /**
     * Found in: https://stackoverflow.com/questions/7111651/how-to-generate-a-secure-random-alphanumeric-string-in-java-efficiently
     *
     * @param lenght the length of one_time_password of the one-time password
     * @return a one-time password
     */
    public String generateOneTimePassword(int lenght) throws NoSuchAlgorithmException {

        final String chrs = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        final SecureRandom secureRandom = SecureRandom.getInstanceStrong();

        final String one_time_password = secureRandom
                .ints(lenght, 0, chrs.length()) // 9 is the lengthOfone_time_password of the string you want
                .mapToObj(i -> chrs.charAt(i))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        return one_time_password;

    }

    @Transactional
    public void insertOneTimePasswordIntoDatabase(String one_time_password, String first_name, String last_name) {

        OneTimePasswordEntity oneTimePasswordEntity = new OneTimePasswordEntity(one_time_password, first_name, last_name);
        // persist the one-time password
        oneTimePasswordEntityRepository.persist(oneTimePasswordEntity);

    }

}
