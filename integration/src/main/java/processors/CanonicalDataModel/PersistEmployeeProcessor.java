package processors.CanonicalDataModel;

import Entities.Employee;
import Entities.Employment_status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.EmployeeRepository;
import repositories.Employment_statusRepository;

@ApplicationScoped
public class PersistEmployeeProcessor implements Processor {


    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    Employment_statusRepository employment_statusRepository;

    Logger logger = LoggerFactory.getLogger(PersistEmployeeProcessor.class);

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


            //TODO: How to catch exception if email is not unique?

            // Persist
            QuarkusTransaction.requiringNew().run(() -> {
                // #############################################
                // TODO: Find better way init employment status
                // When done like this, it will create a new employment status every time
                Employment_status employment_status = employment_statusRepository.find("status", employee.getEmployment_status().toString()).firstResult();
                if (employment_status == null) {
                    employment_status = new Employment_status();
                    employment_status.setStatus(employee.getEmployment_status().getStatus());
                    employment_statusRepository.persist(employment_status);
                }

                employee.setEmployment_status(employment_status);
                // #############################################

                employeeRepository.persist(employee);
            });
            logger.info("Employee '"+ employee.getEmail() + "' persisted successfully");
            exchange.getMessage().setBody("Employee '"+ employee.getEmail() + "' persisted successfully");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);


        } catch (Exception e) {

            logger.error("Error while persisting employee");
            exchange.getMessage().setBody("Employee not persisted, error while persisting employee");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
        }
    }
}
