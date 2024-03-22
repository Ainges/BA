package processors.AuthorityAPI;

import Singeltons.OkHttpClientSingelton;
import okhttp3.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                logger.info("User '" + exchange.getMessage().getHeader("email", String.class)+ "' successfully created in authority. ");
                exchange.getMessage().setBody(response.body().string());
            } else {
                exchange.getMessage().setBody("Failed to create user in authority: " + response.body().string());
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
            }
        }
    }
}
