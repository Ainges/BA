package processors.deprecated;

import DTO.EmployeeDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * This class gets all users form the Authority database and converts them to a DTO and saves them in a header for further processing.
 */
public class GetAllUsersProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(GetAllUsersProcessor.class);

    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    @Override
    public void process(Exchange exchange) {

        logger.info("Requesting Users from Authority DB...");

        try {

            // Get the database connection of Authority
            String url = ConfigProvider.getConfig().getValue("quarkus.datasource.authority.jdbc.url", String.class);
            String username = ConfigProvider.getConfig().getValue("quarkus.datasource.authority.username", String.class);
            String password = ConfigProvider.getConfig().getValue("quarkus.datasource.authority.password", String.class);

            Connection connection = DriverManager.getConnection(url, username, password);

            String sqlFilePath = "src/main/resources/sql/allUserDataQuery.sql";
            String sql = new String(Files.readAllBytes(Path.of(sqlFilePath)), StandardCharsets.UTF_8);

            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            // Empty list of users
            List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();


            while (resultSet.next()) {

                // String 1: id -> 9a746568-37aa-4d82-b02c-addfc7adebb1
                // String 2: username -> Michael
                // String 3: email -> michael.scott@dundermifflin.com
                // String 4: fullName -> Michael Scott
                // String 5: picture -> localhost:8080/api/employee/picture/michael.scott@dundermifflin.com
                // String 6: company and position -> Dunder Mifflin - Regional Manager

                EmployeeDTO employeeDTO = new EmployeeDTO(
                        resultSet.getString(resultSet.findColumn("accountId")),
                        resultSet.getString(resultSet.findColumn("userName")),
                        resultSet.getString(resultSet.findColumn("email")),
                        resultSet.getString(resultSet.findColumn("fullName")),
                        resultSet.getString(resultSet.findColumn("picture")),
                        resultSet.getString(resultSet.findColumn("company"))

                );

                // Exclude admin and test user
                if (employeeDTO.getUsername().equals("admin") ||
                    employeeDTO.getUsername().equals("test")) {
                    continue;
                }

                    employeeDTOList.add(employeeDTO);


            }

            // format the list of employees to json and set it as the body of the exchange
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(employeeDTOList);
            // format json pretty
            exchange.getMessage().setBody(json);

        } catch (Exception e) {
            logger.error("ERROR WHILE GETTING ALL USERS: " + e.getMessage());
        }
        logger.info("...Received Users.");
    }
}
