package processors.CanonicalDataModel;


import DTO.CanonicalUserDTO;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class HashPasswordProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        CanonicalUserDTO canonicalUserDTO = exchange.getMessage().getHeader("CanonicalUserDTO",CanonicalUserDTO.class);
        String password = canonicalUserDTO.getPassword();
        // hash password
        // salt is generated automatically
        String hashedPassword = BcryptUtil.bcryptHash(password);

        canonicalUserDTO.setPassword(hashedPassword);

        exchange.getMessage().setHeader("CanonicalUserDTO", canonicalUserDTO);



    }
}
