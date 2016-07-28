package api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MGSchmidt on 7/28/2016.
 */
public class TokenValidation {

    private boolean valid;

    public TokenValidation(boolean valid) {
        this.valid = valid;
    }

    @JsonProperty
    public boolean isValid() {
        return valid;
    }
}
