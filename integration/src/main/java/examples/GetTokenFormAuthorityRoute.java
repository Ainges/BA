package examples;

import CDI.TokenManagerAuthority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GetTokenFormAuthorityRoute extends RouteBuilder {

    @Inject
    TokenManagerAuthority tokenManagerAuthority;

    Logger logger = LoggerFactory.getLogger(GetTokenFormAuthorityRoute.class);

    @Override
    public void configure() throws Exception {

        /*from("timer:foo?period=5000")
                .process(exchange -> {
                    logger.info("Token: " + tokenManagerAuthority.getToken());
                })
                .to("log:foo");*/
    }
}
