package CDI;

import DTO.AuthorityTokenResponseDTO;
import Singeltons.OkHttpClientSingelton;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
public class TokenManagerAuthority {
    Logger logger = LoggerFactory.getLogger(TokenManagerAuthority.class);
    private String token;
    private Instant expirationTime;
    private AuthorityTokenResponseDTO rawResponse;

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
            setToken(this.rawResponse.getAccess_token(), calculateExpirationTime());
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

        //TODO: replace with dynamic values from application.properties
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", "camel")
                .add("client_secret", "camel")
                .add("scope", "upe_admin")
                .build();

        // TODO: replace with url from application.properties
        Request request = new Request.Builder()
                .url("http://localhost:11560/token")
                .post(requestBody)
                .build();

        // send request
        try (Response response = client.newCall(request).execute()) {

            String responseBody = response.body().string();

            // map response to AuthorityTokenResponseDTO
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                AuthorityTokenResponseDTO authorityTokenDTO = objectMapper.readValue(responseBody, AuthorityTokenResponseDTO.class);

                this.rawResponse = authorityTokenDTO;

            } catch (Exception ex) {
                logger.error("Error parsing response body of Token Request: " + responseBody);
                ex.printStackTrace();
            }


        } catch (IOException e) {
            logger.error("Error obtaining new token");
            e.printStackTrace();
        }

    }

    private Instant calculateExpirationTime() {

        // renew token 10 seconds before it expires
        int expires_in_with_threshold = Integer.parseInt(this.rawResponse.getExpires_in()) - 10;

        return Instant.now().plus(Duration.ofSeconds(expires_in_with_threshold));
    }
}
