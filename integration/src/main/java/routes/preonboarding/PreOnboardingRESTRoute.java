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

                // ********** Moving Request **********
                .post("/MovingRequest")
                .consumes("application/json")
                .to("direct:MovingRequest")

                .get("/MovingRequest/accept")
                .produces("text/html")
                .to("direct:MovingRequestAccept")

                .get("/MovingRequest/decline")
                .produces("text/html")
                .to("direct:MovingRequestDecline")

                // ********** Send Welcome Message To New Employee **********
                .post("/FirstWelcomeMessage")
                .consumes("application/json")
                .to("direct:FirstWelcomeMessageToBroker")

                // ********** Info For First Working Day **********
                .post("InfoForFirstWorkingDay")
                .consumes("application/json")
                .to("direct:InfoForFirstWorkingDayToBroker")

                // Friendly Reminder
                .post("FriendlyReminder")
                .consumes("application/json")
                .to("direct:FriendlyReminderToBroker")


                // Birthday Calculation
                .post("BirthdayCalculation")
                .consumes("application/json")
                .to("direct:BirthdayCalculationToBroker")

                // Available Equipment
                .get("AvailableEquipment")
                .produces("application/json")
                .to("direct:AvailableEquipment")

        ;




    }
}
