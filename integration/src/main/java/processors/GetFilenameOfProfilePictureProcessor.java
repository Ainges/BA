package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetFilenameOfProfilePictureProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(GetFilenameOfProfilePictureProcessor.class);

    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    @Override
    public void process(Exchange exchange) throws Exception {

        logger.info("trying to retrieve filename of profile picture from S3...");

        // DB call to get the address of the profile picture

        // load config
        String url = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);
        String username = ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);
        String password = ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);

        // create connection
        Connection connection = DriverManager.getConnection(url, username, password);

        String sql = "SELECT file_name FROM ba.employee_picture_mapping WHERE email = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, exchange.getMessage().getHeader("email", String.class));

        logger.info("Executing SQL statement: " + preparedStatement.toString());

        ResultSet resultSet = preparedStatement.executeQuery();

        // get String from result set
        String picture_file_name = null;
        while (resultSet.next()) {
            picture_file_name = resultSet.getString("file_name");
        }

        if(picture_file_name == null) {
            logger.error("No profile picture found for email: " + exchange.getMessage().getHeader("email", String.class));
            throw new Exception("No profile picture found for email: " + exchange.getMessage().getHeader("email", String.class));
        }
        logger.info("Profile picture found for email: " + exchange.getMessage().getHeader("email", String.class));
        logger.info("Filename of profile picture: " + picture_file_name);
        exchange.getMessage().setHeader("picture_file_name", picture_file_name);

        logger.info("Retrieved filename of profile picture from S3");
    }
}
