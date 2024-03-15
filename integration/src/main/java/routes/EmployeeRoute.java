package routes;

import org.apache.camel.builder.RouteBuilder;
import processors.GetFilenameOfProfilePictureProcessor;
import processors.GetAllUsersProcessor;
import processors.GetProfileFromS3Processor;

public class EmployeeRoute extends RouteBuilder {

    public void configure() throws Exception {

        rest("/employee")
                .get("/all/")
                .produces("application/json")
                .to("direct:getAllEmployees")
                .get("/picture/{email}")
                .to("direct:getProfilePicture");


        from("direct:getAllEmployees")
                .id("get-all-employees-route")
                .log("Getting all employees")
                .process(new GetAllUsersProcessor())
                .to("log: All employees: ${body}");

        from("direct:getProfilePicture")
                .id("get-profile-picture-route")
                .log("Getting profile picture for ${header.email}")
                .process(new GetFilenameOfProfilePictureProcessor())
                .log("Path to profile picture sucessfully retrieved")
                .process(new GetProfileFromS3Processor())
                .log("log: Profile picture sucessfully returned!");


    }
}
