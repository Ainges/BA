package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import processors.PreOnboarding.InfoForFirstWorkingDayProcessor;

@ApplicationScoped
public class PreOnboardingInfoForFirstWorkingDay extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */

    @Inject
    InfoForFirstWorkingDayProcessor infoForFirstWorkingDayProcessor;

    @Override
    public void configure() throws Exception {

        from("direct:InfoForFirstWorkingDayToBroker")
                .routeId("InfoForFirstWorkingDay-To-Broker-Route")
                .log("Sending infos for first working day to Artemis...")
                .to(ExchangePattern.InOnly, "jms:queue:InfoForFirstWorkingDay");


        from("jms:queue:InfoForFirstWorkingDay")
                .routeId("InfoForFirstWorkingDay-Route")
                .log("Processing \"InfoForFirstWorkingDay\" from Artemis: ${body}")
                .process(infoForFirstWorkingDayProcessor)
                .log("Sending email 'Info-For-First_Working_Day' to new employee...")
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}")
                .log("Email 'Info-For-First_Working_Day' successfully sent to new employee!");
        
    }
}
