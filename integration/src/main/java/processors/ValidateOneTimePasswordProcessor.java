package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import static org.apache.camel.language.constant.ConstantLanguage.constant;

public class ValidateOneTimePasswordProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {



        // in case of successful validation
        exchange = failure(exchange);


    }

    public Exchange success(Exchange exchange) {
        //TODO: Extract the HTML to a separate file
        //TODO: build pretty HTML
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Erfolgreich</title>
                </head>
                <body>
                    <h1>Danke für Ihre Rückmeldung</h1>
                    <p>Gerne unterstützen wir Sie bei ihrem Umzug! Ein Ansprechpartner wird sich in Kürze bei Ihnen melden.</p>
                </body>
                </html>
                """;

        exchange.getMessage().setHeader("Content-Type", constant("text/html;charset=UTF-8"));
        exchange.getMessage().setBody(html);
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

    public boolean validateOneTimePassword(String oneTimePassword) {



        //TODO: Implement the validation of the one-time password
        return true;
    }
}
