package processors.CanonicalDataModel;

import DTO.Employee.EmployeeAllAttributesANDProfilePictureDTO;
import Entities.Employee;
import Entities.ProfilepicturePath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import repositories.EmployeeRepository;
import repositories.ProfilePicturePathRepository;

@ApplicationScoped
public class GetCanonicalUserByMailProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    ProfilePicturePathRepository profilePicturePathRepository;

    @ConfigProperty(name = "minio.url")
    private String profile_picture_url_prefix;

    @Override
    public void process(Exchange exchange) throws Exception {

        String searchMail = exchange.getMessage().getHeader("email", String.class);

        EmployeeAllAttributesANDProfilePictureDTO searchedEmployeeDTO = getEmployeeByMail(searchMail);



        if (searchedEmployeeDTO == null) {
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
            exchange.getMessage().setBody("No user found with email: " + searchMail);
            return;
        }
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getMessage().setBody(searchedEmployeeDTO.toString());


    }

    @Transactional
    public EmployeeAllAttributesANDProfilePictureDTO getEmployeeByMail(String searchMail) {
        Employee searchedEmployee = employeeRepository.findByEmail(searchMail);

        EmployeeAllAttributesANDProfilePictureDTO employeeAllAttributesANDProfilePictureDTO = new EmployeeAllAttributesANDProfilePictureDTO();
        // Only set email, position, first_name and last_name

        employeeAllAttributesANDProfilePictureDTO.setEmail(searchedEmployee.getEmail());
        employeeAllAttributesANDProfilePictureDTO.setPosition(searchedEmployee.getPosition());
        employeeAllAttributesANDProfilePictureDTO.setFirst_name(searchedEmployee.getFirst_name());
        employeeAllAttributesANDProfilePictureDTO.setLast_name(searchedEmployee.getLast_name());

        //enhanche with profile picture
        ProfilepicturePath profilepicturePath = profilePicturePathRepository.findById(searchedEmployee.getId());

        if (profilepicturePath != null) {
            employeeAllAttributesANDProfilePictureDTO.setProfile_picture_url(profile_picture_url_prefix + profilepicturePath.getPath());
        }

        return employeeAllAttributesANDProfilePictureDTO;
    }
}
