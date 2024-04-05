package routes;

import CDI.TokenManagerAuthority;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.AuthorityAPI.CreateUserInAuthorityProcessor;


@ApplicationScoped
public class ManageAuthorityUserRoutes extends RouteBuilder {

    Logger logger = LoggerFactory.getLogger(ManageAuthorityUserRoutes.class);

    @Inject
    TokenManagerAuthority tokenManagerAuthority;

    @Override
    public void configure() throws Exception  {

        rest("/api/authority/user/")
                .post("/create/")
                .to("direct:createAuthorityUser");


        from("direct:createAuthorityUser")
                .id("create-Authority-User-Route")
                .to("direct:getToken")
                .process(exchange -> {
                    String body = exchange.getMessage().getBody(String.class);

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(body);

                    String email = jsonNode.get("email").asText();
                    String password = jsonNode.get("password").asText();

                    if (email == null || password == null) {
                        logger.error("Could not create user in authority, email or password is null");
                        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                        return;
                    }



                    exchange.getMessage().setHeader("email", email);
                    exchange.getMessage().setHeader("password", password);

                })
                .process( new CreateUserInAuthorityProcessor());



        // Get token from authority Route
        from ("direct:getToken")
                .id("get-Token-of-Authority-Route")
                .process(exchange -> {
                    exchange.getMessage().setHeader("Authorization", "Bearer " + tokenManagerAuthority.getToken());
                });


    }
}
