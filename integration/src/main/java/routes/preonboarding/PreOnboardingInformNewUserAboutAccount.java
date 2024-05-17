package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import processors.PreOnboarding.InformNewUserAboutAccountProcessor;

@ApplicationScoped
public class PreOnboardingInformNewUserAboutAccount extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */

    @Inject
    InformNewUserAboutAccountProcessor informNewUserAboutAccountProcessor;

    @Override
    public void configure() throws Exception {


        from("direct:InformNewUserAboutAccountToBroker")
                .routeId("Inform-New-User-About-Account-to-Broker-Route")
                .log("InformNewUserAboutAccount: Sending to Broker... ${body}")
                .to("jms:queue:InformNewUserAboutAccount");


        from("jms:queue:InformNewUserAboutAccount")
                .id("Inform-New-User-About-Account-from-Broker-Route")
                .log("InformNewUserAboutAccount: message received from Broker... ${body}")
                .process(
                        informNewUserAboutAccountProcessor
                )
                .log("InformNewUserAboutAccount: Sending email to new user...")
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}")
                .process(exchange -> {
                    exchange.getMessage().setBody("Email 'InformNewUserAboutAccount' sent!");
                    exchange.getMessage().setHeader("CamelHttpResponseCode", 200);
                })
                .log("InformNewUserAboutAccount: Email sent!");


    }
}
