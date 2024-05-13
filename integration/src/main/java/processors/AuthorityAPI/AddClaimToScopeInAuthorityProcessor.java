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
public class AddClaimToScopeInAuthorityProcessor implements Processor {

    @ConfigProperty(name = "authority.url")
    String authority_url;

    Logger logger = LoggerFactory.getLogger(AddClaimToScopeInAuthorityProcessor.class);

    String scopeName = "engine_read";

    @Override
    public void process(Exchange exchange) throws Exception {

        OkHttpClient client = OkHttpClientSingelton.getInstance();
        String body = exchange.getMessage().getBody(String.class);

        JSONObject jsonObject = new JSONObject(body);

        String claimName = jsonObject.getString("name");

        RequestBody requestBody = new FormBody.Builder()
                .add("claimName", claimName)
                .build();

                Request request = new Request.Builder()
                // Scope is hardcoded to 'engine_read' for now
                .url(authority_url + "/acr/username_password/admin/scope/" + scopeName + "/add/claim")
                .header("Authorization", exchange.getMessage().getHeader("Authorization", String.class))
                .patch(requestBody)
                .build();

                // try...catch block

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String success = "Claim '" + claimName + "' successfully added to Scope '"+scopeName+"'.";
                    logger.info(success);
                    exchange.getMessage().setBody(success);
                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
                } else {
                    String error = "Failed to add Claim '" + claimName + "' to Scope '"+scopeName+"'.";
                    logger.error(error);
                    exchange.getMessage().setBody(error);
                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
                }
            }


        catch (Exception e){
            logger.error("Exception in CreateClaimInAuthorityProcessor: " + e.getMessage());
        }



    }
}
