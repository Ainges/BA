package examples.hibernate;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jetbrains.annotations.NotNull;

@ApplicationScoped
public class HibernateTestProcessor implements Processor {


    @Inject
    BenutzerRepository benutzerRepository;

    @Inject
    RoleRepository roleRepository;


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
        benutzer.setName(userDTO.name);

        // Create if not exists
        Role role = roleRepository.find("roleName", userDTO.roleName).firstResult();
        if (role == null) {
            role = new Role();
            role.setRoleName(userDTO.roleName);
            roleRepository.persist(role);
        }

        benutzer.setRole(role);


        benutzerRepository.persist(benutzer);
    }


}
