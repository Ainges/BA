package examples.hibernate;

import CDI.TokenManagerAuthority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class HiberanteTestRoute extends RouteBuilder {

    @Inject
    TokenManagerAuthority tokenManagerAuthority;


    // You have to Inject the Processor to have CDI Injection working inside the Processor!
    @Inject
    HibernateTestProcessor hibernateTestProcessor;

    Logger logger = LoggerFactory.getLogger(HiberanteTestRoute.class);

    @Override
    public void configure() throws Exception {

        from("timer:test?period=10000000")
                .id("Panache-Test-Route")
                .process(exchange -> {

                    logger.info("Token: " + tokenManagerAuthority.getToken());

                })
                .process(hibernateTestProcessor)
                .log("Panache test route finished! sleeping now... ");


    }
}
