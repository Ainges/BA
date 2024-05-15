package routes.usermanagement;

import CDI.TokenManagerAuthority;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.AuthorityAPI.AddClaimToScopeInAuthorityProcessor;
import processors.AuthorityAPI.AddScopeToUserInAuthorityProcessor;
import processors.AuthorityAPI.CreateClaimInAuthorityProcessor;
import processors.AuthorityAPI.CreateUserInAuthorityProcessor;


@ApplicationScoped
public class ManageAuthorityUserRoutes extends RouteBuilder {

    Logger logger = LoggerFactory.getLogger(ManageAuthorityUserRoutes.class);

    @Inject
    TokenManagerAuthority tokenManagerAuthority;

    @Inject
    CreateUserInAuthorityProcessor createUserInAuthorityProcessor;

    @Inject
    CreateClaimInAuthorityProcessor createClaimInAuthorityProcessor;

    @Inject
    AddClaimToScopeInAuthorityProcessor addClaimToScopeInAuthorityProcessor;

    @Inject
    AddScopeToUserInAuthorityProcessor addScopeToUserInAuthorityProcessor;

    @Override
    public void configure() throws Exception  {

        rest("/api/authority/")

                .post("/claim/create/")
                .to("direct:createAuthorityClaim")

                .post("/user/create/")
                .to("direct:createAuthorityUser")

                .post("/scope/add/claim")
                .to("direct:addClaimToScope")

                .post("/user/add/scope")
                .to("direct:addScopeToUser");

        /*
         * Get Token from Authority
         * */
        from ("direct:getToken")
                .id("get-Token-of-Authority-Route")
                .process(exchange -> {
                    exchange.getMessage().setHeader("Authorization", "Bearer " + tokenManagerAuthority.getToken());
                });

        /*
         * Create User in Authority
         * */
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
                .process(createUserInAuthorityProcessor)
                .end();

        /*
         * Create Claim in Authority
         * */
        from("direct:createAuthorityClaim")
                .id("create-Authority-Claim-Route")
                .to("direct:getToken")
                .process(createClaimInAuthorityProcessor)
                .end();


        /*
        * Add Claim to Scope
        * */
        from("direct:addClaimToScope")
                .id("add-Claim-to-Scope-Route")
                .to("direct:getToken")
                .process(addClaimToScopeInAuthorityProcessor);

        /*
        * Add Scope to User
        * */

        from("direct:addScopeToUser")
                .id("add-Scope-to-User-Route")
                .to("direct:getToken")
                .process(addScopeToUserInAuthorityProcessor);


    }
}
