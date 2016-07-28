package api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MGSchmidt on 7/28/2016.
 */
public class TokenValidation {

    private boolean valid;
    private String userID;

    public TokenValidation() {

    }

    public TokenValidation(boolean valid, String userID) {
        this.valid = valid;
        this.userID = userID;
    }

    @JsonProperty
    public boolean isValid() {
        return valid;
    }

    @JsonProperty
    public String getUserID() {
        return userID;
    }
}
