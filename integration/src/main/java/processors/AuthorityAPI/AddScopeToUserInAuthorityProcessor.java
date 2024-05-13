package processors.AuthorityAPI;

import Singeltons.OkHttpClientSingelton;
import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class AddScopeToUserInAuthorityProcessor implements Processor {

    @ConfigProperty(name = "authority.url")
    String authority_url;

    String scopeName = "engine_read";

    Logger logger = LoggerFactory.getLogger(AddScopeToUserInAuthorityProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        OkHttpClient client = OkHttpClientSingelton.getInstance();
        String body = exchange.getMessage().getBody(String.class);

        JSONObject jsonObject = new JSONObject(body);

        String email = jsonObject.getString("email");


        RequestBody requestBody = new FormBody.Builder()
                .add("scopeName", scopeName)
                .build();

        Request request = new Request.Builder()
                .url(authority_url + "/acr/username_password/admin/user/" + email + "/add/scope")
                .header("Authorization", exchange.getMessage().getHeader("Authorization", String.class))
                .patch(requestBody)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String success = "Scope '" + scopeName + "' successfully added to User '" + email + "'.";
                logger.info(success);
                exchange.getMessage().setBody(success);
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
            } else {
                String error = "Failed to add Scope '" + scopeName + "' to User '" + email + "'.";
                logger.error(error);
                exchange.getMessage().setBody(error);
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
            }
        }


    }
}
