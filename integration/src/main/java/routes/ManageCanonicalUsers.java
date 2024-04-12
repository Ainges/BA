package routes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import processors.CanonicalDataModel.GetAllCanonicalUsersProcessor;
import processors.CanonicalDataModel.PersistEmployeeProcessor;

@ApplicationScoped
public class ManageCanonicalUsers extends RouteBuilder {


    @Inject
    PersistEmployeeProcessor persistEmployeeProcessor;

    @Inject
    GetAllCanonicalUsersProcessor getAllCanonicalUsersProcessor;


    @Override
    public void configure() throws Exception {

        rest("api/canonical/user/")
                .post("/create/")
                .to("direct:createCanonicalUser").consumes("application/json").produces("application/json")

                .get("").to("direct:getAllUsers").produces("application/json");


        from("direct:createCanonicalUser")
                .id("create-Canonical-User-Route")
                // map to EmployeeAllAttributesDTO
                .process(persistEmployeeProcessor);

        from("direct:getAllUsers")
                .id("get-All-Canonical-Users-Route")
                .process(getAllCanonicalUsersProcessor);
    }
}
