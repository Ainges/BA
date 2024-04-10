package routes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import processors.init_users.InitProfilePicturesProcessor;

@ApplicationScoped
public class InitUsersRoute extends RouteBuilder {

    @ConfigProperty(name = "engine.bearer")
    String bearerToken;

    @Inject
    InitProfilePicturesProcessor initProfilePicturesProcessor;

    @Override
    public void configure() throws Exception {

        rest("init_users")
                .post("/init_profile_pictures")
                .to("direct:init_profile_pictures");


        from("timer://runOnce?repeatCount=1&delay=2000")
                .id("init-users-route")
                .log("Initializing users...")
                .pollEnrich("file:src/main/resources/init/?fileName=init_users.json&noop=true")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader("Authorization", constant("Bearer " + bearerToken))
                .to("http://localhost:8000/atlas_engine/api/v1/messages/init_users/trigger")
                .log("Process started...")
                ;

        from("direct:init_profile_pictures")
                .process(initProfilePicturesProcessor)
                .log("Profile pictures initialized");

    }
}
