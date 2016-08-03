package views;

import io.dropwizard.views.View;

public class RegisterSecurityKeyView extends View {
    private String username;
    private String challenge;
    private String APP_ID;

    public RegisterSecurityKeyView(String username, String challenge, String APP_ID) {
        super("register_security_key.ftl");

        this.username = username;
        this.challenge = challenge;
        this.APP_ID = APP_ID;
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
