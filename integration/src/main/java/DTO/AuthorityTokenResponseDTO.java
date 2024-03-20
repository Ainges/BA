package DTO;

public class AuthorityTokenResponseDTO {
    private String access_token;
    private String expires_in;
    private String token_type;
    private String scope;

    public AuthorityTokenResponseDTO(String access_token, String expires_in, String token_type, String scope) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.scope = scope;
    }

    public AuthorityTokenResponseDTO() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
