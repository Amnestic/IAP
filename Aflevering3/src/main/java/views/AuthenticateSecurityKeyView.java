package views;

import io.dropwizard.views.View;

public class AuthenticateSecurityKeyView extends View{
    private String username;
    private String challenge;
    private String APP_ID;

    public AuthenticateSecurityKeyView(String username, String challenge, String app_id) {
        super("authenticate_security_key.ftl");
        this.username = username;
        this.challenge = challenge;
        APP_ID = app_id;
    }

    public String getUsername() {
        return username;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getAPP_ID() {
        return APP_ID;
    }
}
