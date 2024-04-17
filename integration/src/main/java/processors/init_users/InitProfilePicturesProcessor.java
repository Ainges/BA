package processors.init_users;

import Entities.Employee;
import Entities.ProfilepicturePath;
import Singeltons.OkHttpClientSingelton;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.EmployeeRepository;
import repositories.ProfilePicturePathRepository;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@ApplicationScoped
public class InitProfilePicturesProcessor implements Processor {

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    ProfilePicturePathRepository profilePicturePathRepository;

    @Inject
    OkHttpClientSingelton okHttpClientSingelton;

    Logger logger = LoggerFactory.getLogger(InitProfilePicturesProcessor.class);

    @ConfigProperty(name = "engine.host")
    String engineURL;

    @ConfigProperty(name = "engine.port")
    String enginePort;

    @ConfigProperty(name = "engine.bearer")
    String bearerToken;

    @Override
    public void process(Exchange exchange) throws Exception {


        initProfilePictures();

    }

    @Transactional
    public void initProfilePictures() {

        List<Employee> employeeList = employeeRepository.listAll();


        for (Employee employee : employeeList) {
            ProfilepicturePath profilepicturePath = new ProfilepicturePath();
            profilepicturePath.setEmployee(employee);
            profilepicturePath.setPath(
                    "/profilepictures/" +
                    employee.getFirst_name().toLowerCase() +
                    employee.getLast_name().toLowerCase() +
                    ".png");

            profilePicturePathRepository.persist(profilepicturePath);
        }
    }

}
