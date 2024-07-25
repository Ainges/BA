package processors.PreOnboarding;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;

@ApplicationScoped
public class CraftEmailFromNameProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */

    @ConfigProperty(name = "data.company.domain")
    String companyDomain;

    @Override
    public void process(Exchange exchange) throws Exception {


        String firstName="";
        String lastName="";

        Map<String, Object> body = exchange.getIn().getBody(Map.class);
        if (body != null && body.containsKey("OnboardingData")) {
            Map<String, Object> onboardingData = (Map<String, Object>) body.get("OnboardingData");
            firstName = (String) onboardingData.get("first_name");
            lastName = (String) onboardingData.get("last_name");

            exchange.getIn().setHeader("first_name", firstName);
            exchange.getIn().setHeader("last_name", lastName);
        }

        if (firstName == null || firstName.isEmpty()) {
            throw new Exception("could not parse first name or lastname from request");
        }


        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + companyDomain;
        exchange.getMessage().setHeader("email", email);
    }
}
