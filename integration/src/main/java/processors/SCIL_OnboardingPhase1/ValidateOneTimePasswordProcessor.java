package processors.SCIL_OnboardingPhase1;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static org.apache.camel.language.constant.ConstantLanguage.constant;

public class ValidateOneTimePasswordProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(ValidateOneTimePasswordProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        logger.info("Validating request...");

        String oneTimePassword = exchange.getMessage().getHeader("oneTimePassword").toString();
        logger.info("Got: oneTimePassword: " + oneTimePassword);
        String firstName = exchange.getMessage().getHeader("firstName").toString();
        logger.info("Got: firstName: " + firstName);
        String lastName = exchange.getMessage().getHeader("lastName").toString();
        logger.info("Got: lastName: " + lastName);

        if (validateOneTimePassword(oneTimePassword, firstName, lastName)) {
            logger.info("One-time password is valid");
            exchange = success(exchange);
        } else {
            logger.info("One-time password is invalid! \n Abborting...");
            exchange = failure(exchange);
        }


    }

    public Exchange success(Exchange exchange) {
        //TODO: Extract the HTML to a separate file
        //TODO: build pretty HTML
        String htmlAccepted = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Erfolgreich</title>
                </head>
                <body>
                    <h1>Ihre Auswahl wurde gespeichert!</h1>
                    <p>Gerne unterstützen wir Sie bei ihrem Umzug! Ein Ansprechpartner wird sich in Kürze bei Ihnen melden.</p>
                </body>
                </html>
                """;

        String htmlDeclined = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Erfolgreich</title>
                </head>
                <body>
                    <h1>Ihre Auswahl wurde gespeichert!</h1>
                    <p>Sollten Sie noch fragen haben, wenden Sie sich gerne an uns.</p>
                </body>
                </html>
                """;

        exchange.getMessage().setHeader("Content-Type", constant("text/html;charset=UTF-8"));

        if (exchange.getMessage().getHeader("answerOfNewEmployee").equals("accepted")) {
            exchange.getMessage().setBody(htmlAccepted);
            logger.info("Responding to acceptance of moving request.");
        } else {
            exchange.getMessage().setBody(htmlDeclined);
            logger.info("Responding to declination of moving request.");
        }
        return exchange;
    }

    public Exchange failure(Exchange exchange) {

        //TODO: Extract the HTML to a separate file
        //TODO: build pretty HTML
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Ungültiger Code</title>
                </head>
                <body>
                    <h1>Ungültiger Code</h1>
                    <p>Der eingegebene Code ist ungültig. Bitte versuchen Sie es erneut.</p>
                </body>
                </html>
                """;

        exchange.getMessage().setHeader("Content-Type", constant("text/html;charset=UTF-8"));
        exchange.getMessage().setBody(html);
        return exchange;
    }

    public boolean validateOneTimePassword(String oneTimePassword, String firstName, String lastName) {

        // load config
        String url = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);
        String username = ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);
        String password = ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);

        // create connection
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // create statement
            String sql = "SELECT * FROM ba.onetimepasswords WHERE onetimepassword = ? AND firstname = ? AND lastname = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, oneTimePassword);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                // delete the one-time password from the database
                String deleteSql = "DELETE FROM ba.onetimepasswords WHERE onetimepassword = ? AND firstname = ? AND lastname = ?";
                PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteSql);
                deletePreparedStatement.setString(1, oneTimePassword);
                deletePreparedStatement.setString(2, firstName);
                deletePreparedStatement.setString(3, lastName);
                deletePreparedStatement.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error when validating the password with the database");

        }
        // TODO: is this the correct way to handle this?
        return false;
    }
}
