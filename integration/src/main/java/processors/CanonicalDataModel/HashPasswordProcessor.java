package processors.CanonicalDataModel;


import DTO.EmployeeDTO;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class HashPasswordProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        EmployeeDTO employeeDTO = exchange.getMessage().getHeader("EmployeeDTO", EmployeeDTO.class);
        String password = employeeDTO.getPassword();
        // hash password
        // salt is generated automatically
        String hashedPassword = BcryptUtil.bcryptHash(password);

        employeeDTO.setPassword(hashedPassword);

        exchange.getMessage().setHeader("EmployeeDTO", employeeDTO);



    }
}
