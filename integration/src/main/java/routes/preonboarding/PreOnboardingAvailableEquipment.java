package routes.preonboarding;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreOnboardingAvailableEquipment extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */

    Logger logger = LoggerFactory.getLogger(PreOnboardingAvailableEquipment.class);


    @Override
    public void configure() throws Exception {

        from("direct:AvailableEquipment")
                .id("Available-Equipment-Route")
                // idempotent=false is set because the file is not updated and the same file is read every time
                .pollEnrich("file:src/main/resources/availableEquipment/?fileName=availableEquipment.json&noop=true&idempotent=false", 100)

                .log("Responding to the request with available equipment... ${body}")
                .setHeader("Content-Type", constant("application/json"))
                .end();


    }
}
