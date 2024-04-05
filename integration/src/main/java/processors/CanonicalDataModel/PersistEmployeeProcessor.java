package processors.CanonicalDataModel;

import Entities.Employee;
import Entities.Employment_status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.narayana.jta.QuarkusTransaction;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Logger;

public class PersistEmployeeProcessor implements Processor {

    Logger logger = Logger.getLogger(PersistEmployeeProcessor.class.getName());

    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getMessage().getBody(String.class);

        // map to EmployeeDTO
        ObjectMapper mapper = new ObjectMapper();
        try {
            // print the body
            Employee employee = mapper.readValue(body, Employee.class);
            String password = employee.getPassword();

            // hash password
            // salt is generated automatically
            String hashedPassword = BcryptUtil.bcryptHash(password);
            employee.setPassword(hashedPassword);



            // Persist
            QuarkusTransaction.requiringNew().run(() -> {
                // #############################################
                // TODO: Find better way init employment status
                // When done like this, it will create a new employment status every time
                Employment_status employment_status = Employment_status.find("status", employee.getEmployment_status().toString()).firstResult();
                if (employment_status == null) {
                    employment_status = new Employment_status();
                    employment_status.setStatus(employee.getEmployment_status().getStatus());
                    employment_status.persist();
                }

                employee.setEmployment_status(employment_status);
                // #############################################

                //noinspection Convert2MethodRef
                employee.persist();
            });
            logger.info("Employee persisted successfully");

        } catch (Exception e) {

            logger.info("Error while persisting employee");
            e.printStackTrace();
        }
    }
}
