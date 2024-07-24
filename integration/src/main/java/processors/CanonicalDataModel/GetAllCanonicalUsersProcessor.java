package processors.CanonicalDataModel;

import DTO.Employee.EmployeeAllAttributesANDProfilePictureDTO;
import DTO.Employee.EmployeeAllAttributesDTO;
import Entities.Employee;
import Entities.ProfilepicturePath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.EmployeeRepository;
import repositories.ProfilePicturePathRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GetAllCanonicalUsersProcessor implements Processor {
    /**
     * This Processor returns all canonical users. Cancoical user = Employee at the moment.
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    ProfilePicturePathRepository profilePicturePathRepository;

    @ConfigProperty(name = "minio.external.url")
    private String profile_picture_url_prefix;

    Logger logger = LoggerFactory.getLogger(GetAllCanonicalUsersProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        List<EmployeeAllAttributesANDProfilePictureDTO> responseList = getAllCanonicalUsers();
        if(responseList == null) {
            logger.error("No users found");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
            exchange.getMessage().setBody("No users found");
            return;
        }


        String responseListAsString = StringifyEmployeeAllAttributesANDProfilePictureDTO(responseList);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getMessage().setBody(responseListAsString);



    }

    @Transactional
    public List<EmployeeAllAttributesANDProfilePictureDTO> getAllCanonicalUsers() {
        List<Employee> EmployeeList = employeeRepository.findAll().list();

        List<EmployeeAllAttributesANDProfilePictureDTO> employeeAllAttributesANDProfilePictureDTOList = new ArrayList<>();

        for (Employee employee : EmployeeList) {

            EmployeeAllAttributesANDProfilePictureDTO employeeAllAttributesANDProfilePictureDTO = new EmployeeAllAttributesANDProfilePictureDTO();
            // Only set email, position, first_name and last_name

            employeeAllAttributesANDProfilePictureDTO.setEmail(employee.getEmail());
            employeeAllAttributesANDProfilePictureDTO.setPosition(employee.getPosition());
            employeeAllAttributesANDProfilePictureDTO.setFirst_name(employee.getFirst_name());
            employeeAllAttributesANDProfilePictureDTO.setLast_name(employee.getLast_name());

            employeeAllAttributesANDProfilePictureDTOList.add(employeeAllAttributesANDProfilePictureDTO);

            //enhanche with profile picture
            ProfilepicturePath profilepicturePath = profilePicturePathRepository.findById(employee.getId());

            if(profilepicturePath != null) {
                employeeAllAttributesANDProfilePictureDTO.setProfile_picture_url(profile_picture_url_prefix + profilepicturePath.getPath());
            }

        }

        if(!employeeAllAttributesANDProfilePictureDTOList.isEmpty()) {
            return employeeAllAttributesANDProfilePictureDTOList;
        }
        else {
            return null;
        }
    }

/**
 * @param employeeAllAttributesANDProfilePictureDTOArrayList List of EmployeeAllAttributesANDProfilePictureDTO
 * @return a JsonArray String of the List of EmployeeAllAttributesANDProfilePictureDTO
 * */
    public String StringifyEmployeeAllAttributesANDProfilePictureDTO(List<EmployeeAllAttributesANDProfilePictureDTO> employeeAllAttributesANDProfilePictureDTOArrayList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (EmployeeAllAttributesANDProfilePictureDTO employeeAllAttributesANDProfilePictureDTO : employeeAllAttributesANDProfilePictureDTOArrayList) {
            sb.append(employeeAllAttributesANDProfilePictureDTO.toString());
            // for proper JSON formatting
            if (employeeAllAttributesANDProfilePictureDTOArrayList.indexOf(employeeAllAttributesANDProfilePictureDTO) != employeeAllAttributesANDProfilePictureDTOArrayList.size() - 1) {
                sb.append(",");
            }

        }
        sb.append("]");
        return sb.toString();
    }

}
