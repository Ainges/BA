package processors.CanonicalDataModel;

import DTO.EmployeeDTO;
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

        EmployeeDTO employeeDTO = exchange.getMessage().getHeader("EmployeeDTO", EmployeeDTO.class);

        // Add user to DB

        // load config
        String url = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);
        String username = ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);
        String password = ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);

        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO ba.employees (email, pw, first_name, last_name, position, private_email,employment_status, employment_status, postal_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employeeDTO.getEmail());
            preparedStatement.setString(2, employeeDTO.getPassword());
            preparedStatement.setString(3, employeeDTO.getFirst_name());
            preparedStatement.setString(4, employeeDTO.getLast_name());
            preparedStatement.setString(5, employeeDTO.getPosition());
            preparedStatement.setString(6, employeeDTO.getPrivate_email());
            preparedStatement.setString(7, employeeDTO.getEmployment_status());
            preparedStatement.setString(8, employeeDTO.getPostal_address());
            preparedStatement.executeUpdate();

            logger.info("Canonical User " + employeeDTO.getEmail() + " successfully created.");

        } catch (Exception e) {
            logger.info("Failed to insert canonical User '" + employeeDTO.getEmail() + "' into DB.");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
            exchange.getMessage().setBody("Failed to insert User" + employeeDTO.getEmail() + " into DB.");
            e.printStackTrace();
        }





    }
}
