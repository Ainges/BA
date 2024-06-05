package routes.usermanagement;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import processors.CanonicalDataModel.CheckMailAvailabilityProcessor;
import processors.CanonicalDataModel.GetAllCanonicalUsersProcessor;
import processors.CanonicalDataModel.GetCanonicalUserByMailProcessor;
import processors.CanonicalDataModel.PersistEmployeeProcessor;

@ApplicationScoped
public class ManageCanonicalUsers extends RouteBuilder {


    @Inject
    PersistEmployeeProcessor persistEmployeeProcessor;

    @Inject
    GetAllCanonicalUsersProcessor getAllCanonicalUsersProcessor;

    @Inject
    CheckMailAvailabilityProcessor checkMailAvailabilityProcessor;

    @Inject
    GetCanonicalUserByMailProcessor getCanonicalUserByMailProcessor;


    @Override
    public void configure() throws Exception {

        rest("api/canonical/user/")
                .post("/create/")
                .to("direct:createCanonicalUser").consumes("application/json").produces("application/json")

                .get("")
                .to("direct:getAllUsers")
                .produces("application/json")

                .get("checkmail/{email}")
                .to("direct:checkEmail")
                .produces("application/json")

                .get("/byMail/{email}")
                .to("direct:getCanonicalUserByMail")
                .produces("application/json");


        from("direct:createCanonicalUser")
                .id("create-Canonical-User-Route")
                // map to EmployeeAllAttributesDTO
                .process(persistEmployeeProcessor);

        from("direct:getAllUsers")
                .id("get-All-Canonical-Users-Route")
                .process(getAllCanonicalUsersProcessor);

        from("direct:checkEmail")
                .id("check-Email-Route")
                .process(checkMailAvailabilityProcessor);

        from("direct:getCanonicalUserByMail")
                .id("get-Canonical-User-By-Mail-Route")
                .process(getCanonicalUserByMailProcessor);


    }
}
