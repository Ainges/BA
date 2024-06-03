package routes.onboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processors.Onboarding.FirstPerformanceReviewResultsToNewEmployeeProcessor;

@ApplicationScoped
public class onboardingRESTRoute extends RouteBuilder {

    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */

    Logger logger = LoggerFactory.getLogger(onboardingRESTRoute.class);

    @Inject
    FirstPerformanceReviewResultsToNewEmployeeProcessor firstPerformanceReviewResultsToNewEmployeeProcessor;

    @Override
    public void configure() throws Exception {
        rest("/onboarding/onboarding")
                .post("firstPerformanceReview/sendResultsToNewEmployee")
                .consumes("application/json")
                .produces("application/json")
                .to("direct:sendResultsToNewEmployeeToBroker");



        from("direct:sendResultsToNewEmployeeToBroker")
                .id("send-Results-To-New-Employee-to-Broker-Route")
                .log("FirstPerformanceReviewResultsToNewEmployee: Sending to Broker... ")
                .to("jms:queue:FirstPerformanceReviewResultsToNewEmployee");

        from("jms:queue:FirstPerformanceReviewResultsToNewEmployee")
                .id("send-Results-To-New-Employee-from-Broker-Route")
                .log("FirstPerformanceReviewResultsToNewEmployee: message received from Broker... ")
                .process(
                        firstPerformanceReviewResultsToNewEmployeeProcessor
                )
                .log("FirstPerformanceReviewResultsToNewEmployee: Sending email to new user...")
                .to("smtp://" + "{{smtp.host}}" + ":" + "{{smtp.port}}" + "?username=" + "{{smtp.username}}" + "&password=" + "{{smtp.password}}")
                .process(exchange -> {
                    exchange.getMessage().setBody("Email 'FirstPerformanceReviewResultsToNewEmployee' sent!");
                    exchange.getMessage().setHeader("CamelHttpResponseCode", 200);
                })
                .log("FirstPerformanceReviewResultsToNewEmployee: Email sent!");
    }


}
