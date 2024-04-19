package processors.PreOnboarding;

import Entities.OneTimePasswordEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.OneTimePasswordEntityRepository;

import static org.apache.camel.language.constant.ConstantLanguage.constant;

@ApplicationScoped
public class ValidateOneTimePasswordProcessor implements Processor {

    @Inject
    OneTimePasswordEntityRepository oneTimePasswordEntityRepository;

    Logger logger = LoggerFactory.getLogger(ValidateOneTimePasswordProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        logger.info("Validating request...");

        String one_time_password = exchange.getMessage().getHeader("one_time_password").toString();
        logger.info("Got: one_time_password: " + one_time_password);
        String first_name = exchange.getMessage().getHeader("first_name").toString();
        logger.info("Got: first_name: " + first_name);
        String last_name = exchange.getMessage().getHeader("last_name").toString();
        logger.info("Got: last_name: " + last_name);

        if (validateOneTimePassword(one_time_password, first_name, last_name)) {
            logger.info("One-time password is valid");
            exchange = success(exchange);
        } else {
            logger.info("One-time password is invalid! \n Abborting...");
            exchange = failure(exchange);
        }


    }

    public Exchange success(Exchange exchange) {
        //TODO: Extract the HTML to a separate file
        //TODO: build pretty HTML
        String htmlAccepted = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Erfolgreich</title>
                </head>
                <body>
                    <h1>Ihre Auswahl wurde gespeichert!</h1>
                    <p>Gerne unterstützen wir Sie bei ihrem Umzug! Ein Ansprechpartner wird sich in Kürze bei Ihnen melden.</p>
                </body>
                </html>
                """;

        String htmlDeclined = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Erfolgreich</title>
                </head>
                <body>
                    <h1>Ihre Auswahl wurde gespeichert!</h1>
                    <p>Sollten Sie noch fragen haben, wenden Sie sich gerne an uns.</p>
                </body>
                </html>
                """;

        exchange.getMessage().setHeader("Content-Type", constant("text/html;charset=UTF-8"));

        if (exchange.getMessage().getHeader("answerOfNewEmployee").equals("accepted")) {
            exchange.getMessage().setBody(htmlAccepted);
            logger.info("Responding to acceptance of moving request.");
        } else {
            exchange.getMessage().setBody(htmlDeclined);
            logger.info("Responding to declination of moving request.");
        }
        return exchange;
    }

    public Exchange failure(Exchange exchange) {

        //TODO: Extract the HTML to a separate file
        //TODO: build pretty HTML
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Ungültiger Code</title>
                </head>
                <body>
                    <h1>Ungültiger Code</h1>
                    <p>Der eingegebene Code ist ungültig. Bitte versuchen Sie es erneut.</p>
                </body>
                </html>
                """;

        exchange.getMessage().setHeader("Content-Type", constant("text/html;charset=UTF-8"));
        exchange.getMessage().setBody(html);
        return exchange;
    }

    @Transactional
    public boolean validateOneTimePassword(String one_time_password, String first_name, String last_name) {

        // get one-time password from database with Panache
        //QuarkusTransaction.requiringNew().run(() ->{});
        OneTimePasswordEntity oneTimePasswordEntity = oneTimePasswordEntityRepository.findByPasswordAndName(one_time_password, first_name, last_name);
        if (oneTimePasswordEntity != null) {

            //QuarkusTransaction.requiringNew().run(() -> {
            logger.info("One-time password found in database. Deleting...");
                oneTimePasswordEntityRepository.delete(oneTimePasswordEntity);
            //});


            return true;
        } else {
            return false;
        }

    }
}
