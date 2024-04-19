package routes.preonboarding;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import processors.PreOnboarding.FriendlyReminderProcessor;



@ApplicationScoped
public class PreOnboardingFriendlyReminderRoute extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */


    @ConfigProperty(name = "smtp.host")
    String smtpHost;

    @ConfigProperty(name = "smtp.port")
    String smtpPort;

    @ConfigProperty(name = "smtp.username")
    String smtpUsername;

    @ConfigProperty(name = "smtp.password")
    String smtpPassword;

    @Inject
    FriendlyReminderProcessor friendlyReminderProcessor;

    @Override
    public void configure() throws Exception {
        from("direct:FriendlyReminderToBroker")
                .id("FriendlyReminder-To-Broker-Route")
                .log("Preparing friendly reminder for new employee - sending to Artemis")
                .to(ExchangePattern.InOnly, "jms:queue:FriendlyReminderQueue")
                .log("- sending to Artemis - ...done");

        from("jms:queue:FriendlyReminderQueue")
                .id("FriendlyReminder-From-Broker-Route")
                .log("Message received from Artemis: ${body}")
                .process(friendlyReminderProcessor)
                .to("smtp://" + smtpHost + ":" + smtpPort + "?username=" + smtpUsername + "&password=" + smtpPassword);

    }
}
