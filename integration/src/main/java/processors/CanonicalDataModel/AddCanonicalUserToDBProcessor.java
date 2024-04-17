package processors.CanonicalDataModel;

import DTO.Employee.EmployeeAllAttributesDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Deprecated
public class AddCanonicalUserToDBProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(AddCanonicalUserToDBProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        EmployeeAllAttributesDTO employeeAllAttributesDTO = exchange.getMessage().getHeader("EmployeeAllAttributesDTO", EmployeeAllAttributesDTO.class);

        // Add user to DB

        // load config
        String url = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);
        String username = ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);
        String password = ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);

        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO ba.employees (email, pw, first_name, last_name, position, private_email,employment_status, employment_status, postal_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employeeAllAttributesDTO.getEmail());
            preparedStatement.setString(2, employeeAllAttributesDTO.getPassword());
            preparedStatement.setString(3, employeeAllAttributesDTO.getFirst_name());
            preparedStatement.setString(4, employeeAllAttributesDTO.getLast_name());
            preparedStatement.setString(5, employeeAllAttributesDTO.getPosition());
            preparedStatement.setString(6, employeeAllAttributesDTO.getPrivate_email());
            preparedStatement.setString(7, employeeAllAttributesDTO.getEmployment_status());
            preparedStatement.setString(8, employeeAllAttributesDTO.getPostal_address());
            preparedStatement.executeUpdate();

            logger.info("Canonical User " + employeeAllAttributesDTO.getEmail() + " successfully created.");

        } catch (Exception e) {
            logger.info("Failed to insert canonical User '" + employeeAllAttributesDTO.getEmail() + "' into DB.");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
            exchange.getMessage().setBody("Failed to insert User" + employeeAllAttributesDTO.getEmail() + " into DB.");
            e.printStackTrace();
        }





    }
}
