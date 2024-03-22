package routes;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.CanonicalDataModel.AddCanonicalUserToDBProcessor;
import processors.CanonicalDataModel.HashPasswordProcessor;
import processors.CanonicalDataModel.MapMessageToCanonicalUserDTOProcessor;

public class ManageCanonicalUsers extends RouteBuilder {

    Logger logger = LoggerFactory.getLogger(ManageCanonicalUsers.class);

    @Override
    public void configure() throws Exception {

        rest("api/canonical/user/")
            .post("/create/")
            .to("direct:createCanonicalUser");


        from("direct:createCanonicalUser")
                .id("create-Canonical-User-Route")
                // map to CanonicalUserDTO
                .process( new MapMessageToCanonicalUserDTOProcessor())
                // hash password
                .process(new HashPasswordProcessor())
                // Create user in SCIL DB
                .process(new AddCanonicalUserToDBProcessor())
                .to("log: Canonical User created successfully");

    }
}
