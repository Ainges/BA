package routes.preonboarding;

import org.apache.camel.builder.RouteBuilder;

public class PreOnboardingRESTRoute extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {
        rest("/onboarding/preonboarding")

                // ********** Send Welcome Message To New Employee **********
                .post("/FirstWelcomeMessage")
                .consumes("application/json")
                .to("direct:FirstWelcomeMessageToBroker")

                // ********** Moving Request **********
                .post("/MovingRequest")
                .consumes("application/json")
                .to("direct:MovingRequest")

                .get("/MovingRequest/accept")
                .produces("text/html")
                .to("direct:MovingRequestAccept")
                .get("/MovingRequest/decline")
                .produces("text/html")
                .to("direct:MovingRequestDecline");



    }
}
