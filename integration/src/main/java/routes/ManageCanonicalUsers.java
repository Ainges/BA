package routes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import processors.CanonicalDataModel.PersistEmployeeProcessor;

@ApplicationScoped
public class ManageCanonicalUsers extends RouteBuilder {


    @Inject
    PersistEmployeeProcessor persistEmployeeProcessor;


    @Override
    public void configure() throws Exception {

        rest("api/canonical/user/")
                .post("/create/")
                .to("direct:createCanonicalUser").consumes("application/json").produces("application/json");


        from("direct:createCanonicalUser")
                .id("create-Canonical-User-Route")
                // map to EmployeeDTO
                .process(persistEmployeeProcessor)
                .to("log:info?showAll=true&multiline=true");

    }
}
