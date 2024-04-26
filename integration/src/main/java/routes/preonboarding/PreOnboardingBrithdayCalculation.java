package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import processors.PreOnboarding.CalculateBrithdayProcessor;

@ApplicationScoped
public class PreOnboardingBrithdayCalculation extends RouteBuilder {


    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */

   @Inject
    CalculateBrithdayProcessor calculateBrithdayProcessor;

    @Override
    public void configure() throws Exception {


        from("direct:BirthdayCalculationToBroker")
                .id("BirthdayCalculationToBroker-Route")
                .to("jms:queue:BirthdayCalculation");

        from("jms:BirthdayCalculation")
                .id("BirthdayCalculationFromBroker-Route")
                .process(calculateBrithdayProcessor)
                .to("log: TEST!");


    }
}
