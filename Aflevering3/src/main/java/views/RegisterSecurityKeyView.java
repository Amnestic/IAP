package views;

import io.dropwizard.views.View;

public class RegisterSecurityKeyView extends View {
    private String username;
    private String challenge;

    public RegisterSecurityKeyView(String username, String challenge) {
        super("register_security_key.ftl");

        this.username = username;
        this.challenge = challenge;
    }

    public String getUsername() {
        return username;
    }

    public String getChallenge() {
        return challenge;
    }
}
