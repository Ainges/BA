package processors.CanonicalDataModel;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import repositories.EmployeeRepository;

@ApplicationScoped
public class CheckMailAvailabilityProcessor implements Processor {

    @Inject
    EmployeeRepository employeeRepository;

    @Override
    public void process(Exchange exchange) throws Exception {

        // get email from request path
        String email = exchange.getMessage().getHeader("email", String.class);
        boolean isEmailAvailable = employeeRepository.isEmailAvailable(email);

        exchange.getMessage().setBody("{ \"isEmailAvailable\":" + isEmailAvailable +"}");



    }
}
