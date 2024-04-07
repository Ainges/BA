package processors.AuthorityAPI;

import Singeltons.OkHttpClientSingelton;
import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CreateUserInAuthorityProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(CreateUserInAuthorityProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        OkHttpClient client = OkHttpClientSingelton.getInstance();

        // config
        String authority_url = ConfigProvider.getConfig().getValue("authority.url", String.class);

        // extract email and password from Message body



        RequestBody requestBody = new FormBody.Builder()
            .add("email", exchange.getMessage().getHeader("email", String.class))
            .add("password", exchange.getMessage().getHeader("password", String.class))
            .build();

        Request request = new Request.Builder()
                .url(authority_url + "/acr/username_password/admin/user/email/create")
                .header("Authorization", exchange.getMessage().getHeader("Authorization", String.class))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()){
                String success = "Authority User '" + exchange.getMessage().getHeader("email", String.class)+ "' successfully created.";
                logger.info(success);
                exchange.getMessage().setBody(success);
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
            } else {
                String error = "Failed to create user in authority: '" + exchange.getMessage().getHeader("email", String.class)+ "'.";
                logger.info(error);
                exchange.getMessage().setBody(error);
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
            }
        }
    }
}
