package api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AccessToken implements Serializable {
    public AccessToken() {

    }

    public AccessToken(int accessToken, TokenType tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    @JsonProperty
    public int getAccessToken() {
        return accessToken;
    }

    @JsonProperty
    public TokenType getTokenType() {
        return tokenType;
    }

    private int accessToken;

    private TokenType tokenType;

    public enum TokenType {
        BEARER_TOKEN
    }
}
