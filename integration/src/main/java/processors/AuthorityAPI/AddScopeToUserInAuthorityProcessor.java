package processors.AuthorityAPI;

import Singeltons.OkHttpClientSingelton;
import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONObject;


@ApplicationScoped
public class AddScopeToUserInAuthorityProcessor implements Processor {

    @ConfigProperty(name = "authority.url")
    String authority_url;

    @Override
    public void process(Exchange exchange) throws Exception {

/*        OkHttpClient client = OkHttpClientSingelton.getInstance();
        String body = exchange.getMessage().getBody(String.class);

        JSONObject jsonObject = new JSONObject(body);

        String email = jsonObject.getString("email");

        Request request = new Request.Builder()
                .url(authority_url + "")
                .header("Authorization", exchange.getMessage().getHeader("Authorization", String.class))
                .post(requestBody)
                .build();*/



    }
}
