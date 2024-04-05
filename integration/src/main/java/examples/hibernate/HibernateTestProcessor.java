package examples.hibernate;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jetbrains.annotations.NotNull;

@ApplicationScoped
public class HibernateTestProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {


        UserDTO userDTO = new UserDTO();
        userDTO.name = "Test";
        userDTO.roleName = "Admin";

        QuarkusTransaction.requiringNew().run(() -> {
            testMethode(userDTO);

        });

    }

    public void testMethode(@NotNull UserDTO userDTO) {
        Benutzer benutzer = new Benutzer();
        benutzer.name = userDTO.name;

        // Finden Sie die Rolle anhand des Rollennamens
        Role role = Role.find("roleName", userDTO.roleName).firstResult();
        if (role == null) {
            role = new Role();
            role.roleName = userDTO.roleName;
            role.persist();
        }

        benutzer.role = role;


        benutzer.persist();
    }


}
