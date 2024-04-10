package CDI;

import DTO.AuthorityTokenResponseDTO;
import Singeltons.OkHttpClientSingelton;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.*;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
public class TokenManagerAuthority {

    private String token;
    private Instant expirationTime;
    private AuthorityTokenResponseDTO rawResponse;



    Logger logger = LoggerFactory.getLogger(TokenManagerAuthority.class);

    // is only called by the getToken method
    private synchronized void setToken(String token, Instant expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    /**
     * Returns the token. If the token is expired, a new token is automatically obtained and returned.
     * */
    public synchronized String getToken() {
        if (expirationTime == null || expirationTime.isBefore(Instant.now())) {
            obtainNewToken();


        }
        return token;
    }

    private void obtainNewToken() {

        // post request to localhost:11560/token
        // with x-www-form-urlencoded body with the following key-value pairs:
        // grant_type:client_credentials
        // client_id:camel
        // client_secret:camel
        // scope:upe_admin

        OkHttpClient client = OkHttpClientSingelton.getInstance();

        // load config
        String grant_type_value = ConfigProvider.getConfig().getValue("tokenmanager.authority.grant_type.value", String.class);
        String client_id_value = ConfigProvider.getConfig().getValue("tokenmanager.authority.client_id.value", String.class);
        String client_secret_value = ConfigProvider.getConfig().getValue("tokenmanager.authority.client_secret.value", String.class);
        String scope_value = ConfigProvider.getConfig().getValue("tokenmanger.authority.scope.value", String.class);

        String authority_url = ConfigProvider.getConfig().getValue("authority.url", String.class);

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", grant_type_value)
                .add("client_id", client_id_value)
                .add("client_secret", client_secret_value)
                .add("scope", scope_value)
                .build();

        Request request = new Request.Builder()
                .url(authority_url + "/token")
                .post(requestBody)
                .build();

        // send request
        try (Response response = client.newCall(request).execute()) {

            String responseBody = response.body().string();

            // map response to AuthorityTokenResponseDTO
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                AuthorityTokenResponseDTO authorityTokenDTO = objectMapper.readValue(responseBody, AuthorityTokenResponseDTO.class);

                if (response.isSuccessful()) {
                    logger.info("Token obtained successfully");
                    this.rawResponse = authorityTokenDTO;

                    // Set token and expiration time
                    setToken(this.rawResponse.getAccess_token(), calculateExpirationTime());

                } else {
                    throw new IOException("Request was not successfull! " + response.code() + " " + response.message() + " " + responseBody);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            logger.error("Error obtaining new token - is the authority running?");
            e.printStackTrace();
        }

    }

    private Instant calculateExpirationTime() {

        // renew token 10 seconds before it expires
        int expires_in_with_threshold = Integer.parseInt(this.rawResponse.getExpires_in()) - 10;

        return Instant.now().plus(Duration.ofSeconds(expires_in_with_threshold));
    }
}
