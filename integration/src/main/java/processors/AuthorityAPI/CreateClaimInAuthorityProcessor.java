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
public class CreateClaimInAuthorityProcessor implements Processor {

    @ConfigProperty(name = "authority.url")
    String authority_url;

    Logger logger = LoggerFactory.getLogger(CreateClaimInAuthorityProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {

        OkHttpClient client = OkHttpClientSingelton.getInstance();
        String body = exchange.getMessage().getBody(String.class);

        JSONObject jsonObject = new JSONObject(body);

        String name = jsonObject.getString("name");
        String type = jsonObject.getString("type");
        String description = jsonObject.getString("description");

        RequestBody requestBody = new FormBody.Builder()
                .add("name", name)
                .add("type", type)
                .add("description", description)
                .build();

        Request request = new Request.Builder()
                .url(authority_url + "/acr/username_password/admin/claim/create")
                .header("Authorization", exchange.getMessage().getHeader("Authorization", String.class))
                .post(requestBody)
                .build();

        // try...catch block

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String success = "Authority Claim '" + name + "' successfully created.";
                    logger.info(success);
                    exchange.getMessage().setBody(success);
                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, response.code());
                } else {
                    String error = "Failed to create claim in authority: '" + name + "'.";
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
