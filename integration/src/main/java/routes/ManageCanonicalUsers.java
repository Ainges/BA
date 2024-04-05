package routes;

import org.apache.camel.builder.RouteBuilder;
import processors.CanonicalDataModel.PersistEmployeeProcessor;

public class ManageCanonicalUsers extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        rest("api/canonical/user/")
            .post("/create/")
            .to("direct:createCanonicalUser");


        from("direct:createCanonicalUser")
                .id("create-Canonical-User-Route")
                // map to EmployeeDTO
                .process( new PersistEmployeeProcessor());
    }
}
