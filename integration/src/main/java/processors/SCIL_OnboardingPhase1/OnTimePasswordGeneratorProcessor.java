package processors.SCIL_OnboardingPhase1;

import DTO.NewEmployeeDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Generates and stores a one-time password.
 * This is usefully for sending a one-time password to a new employee, who has not yet received his/her credentials.
 */
public class OnTimePasswordGeneratorProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(OnTimePasswordGeneratorProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        // Generate oneTimePassword and store it in header
        String oneTimePassword = generateOneTimePassword(5);
        logger.info("One-time password generated: " + oneTimePassword);
        exchange.getMessage().setHeader("OneTimePassword", oneTimePassword);

        // Get the body of the message
        String body = exchange.getMessage().getBody().toString();
        NewEmployeeDataDTO newEmployeeDataDTO = new ObjectMapper().readValue(body, NewEmployeeDataDTO.class);

        // Insert one-time password into database
        logger.info("Inserting one-time password into database...");
        insertOneTimePasswordIntoDatabase(oneTimePassword, newEmployeeDataDTO.getFirstName(), newEmployeeDataDTO.getLastName());
        logger.info("One-time password successfully inserted into database.");


    }

    /**
     * Found in: https://stackoverflow.com/questions/7111651/how-to-generate-a-secure-random-alphanumeric-string-in-java-efficiently
     *
     * @param lenght the length of the one-time password
     * @return a one-time password
     */
    public String generateOneTimePassword(int lenght) throws NoSuchAlgorithmException {

        final String chrs = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        final SecureRandom secureRandom = SecureRandom.getInstanceStrong();

        final String oneTimePassword = secureRandom
                .ints(lenght, 0, chrs.length()) // 9 is the length of the string you want
                .mapToObj(i -> chrs.charAt(i))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        return oneTimePassword;

    }

    public void insertOneTimePasswordIntoDatabase(String oneTimePassword, String firstName, String lastName) {


        // load config
        String url = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);
        String username = ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);
        String password = ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);

        // create connection
        try(Connection connection = DriverManager.getConnection(url, username, password)) {

            // create statement
            String sql = "INSERT INTO ba.onetimepasswords (onetimepassword, firstname, lastname) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, oneTimePassword);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.executeUpdate();

        }
        catch (SQLException e){
            logger.error("Error when inserting the onetimepassword into the database ");
        }


    }

}
