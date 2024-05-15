package routes.preonboarding;

import org.apache.camel.builder.RouteBuilder;

public class PreOnboardingAvailableEquipment extends RouteBuilder {
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

        from("direct:AvailableEquipment")
                .id("Available-Equipment-Route")
                .pollEnrich("file:src/main/resources/availableEquipment/?fileName=availableEquipment.json&noop=true")
                .setHeader("Content-Type", constant("application/json"))
                .end();


    }
}
