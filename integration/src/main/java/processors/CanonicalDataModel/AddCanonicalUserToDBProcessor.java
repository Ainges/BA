package processors.CanonicalDataModel;

import DTO.CanonicalUserDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddCanonicalUserToDBProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(AddCanonicalUserToDBProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        CanonicalUserDTO canonicalUserDTO = exchange.getMessage().getHeader("CanonicalUserDTO", CanonicalUserDTO.class);

        // Add user to DB

        // load config
        String url = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);
        String username = ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);
        String password = ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);

        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO ba.canonicalusers (email, password, first_name, last_name, position, profile_picture_url) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, canonicalUserDTO.getEmail());
            preparedStatement.setString(2, canonicalUserDTO.getPassword());
            preparedStatement.setString(3, canonicalUserDTO.getFirst_name());
            preparedStatement.setString(4, canonicalUserDTO.getLast_name());
            preparedStatement.setString(5, canonicalUserDTO.getPosition());
            preparedStatement.setString(6, canonicalUserDTO.getProfile_picture_url());
            preparedStatement.executeUpdate();

            logger.info("Canonical User successfully inserted into DB.");


        } catch (Exception e) {
            logger.info("Failed to insert canonical User into DB.");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
            exchange.getMessage().setBody("Failed to insert User into DB.");
            e.printStackTrace();
        }



    }
}
