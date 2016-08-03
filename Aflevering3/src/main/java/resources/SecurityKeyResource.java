package resources;

import com.yubico.u2f.data.DeviceRegistration;
import com.yubico.u2f.data.messages.RegisterRequestData;
import com.yubico.u2f.data.messages.RegisterResponse;
import db.Database;
import exceptions.InvalidLoginException;
import views.LoginView;
import views.RegisterSecurityKeyView;
import views.IndexView;
import views.RegisterView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.yubico.u2f.U2F;

@Path("/auth")
public class SecurityKeyResource {
    private String APP_ID;
    private Database database;
    private U2F u2f = new U2F();

    public SecurityKeyResource(String APP_ID, Database database) {
        this.APP_ID = APP_ID;
        this.database = database;
    }

    @GET
    public IndexView index() {
        return new IndexView();
    }

    @Path("/register")
    @GET
    public RegisterView register() {
        return new RegisterView();
    }

    @Path("/start_registration")
    @POST
    public RegisterSecurityKeyView registerFlow(@FormParam("username") String username, @FormParam("password") String password) {
        database.insertUser(username, password);
        RegisterRequestData challenge = u2f.startRegistration(APP_ID, database.getDevicesForUser(username));
        database.insertChallenge(username, challenge);
        return new RegisterSecurityKeyView(username, challenge.toJson(), APP_ID);
    }

    @Path("/finish_registration")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String finishRegistration(@FormParam("username") String username, @FormParam("response-input") String responseInput) {
        RegisterRequestData challenge = database.findChallenge(username);
        database.deleteChallenge(username);
        DeviceRegistration registeredDevice = u2f.finishRegistration(challenge, RegisterResponse.fromJson(responseInput));
        database.insertDeviceForUser(username, registeredDevice);
        return "Success! <a href=\"login\">Login here</a>";
    }

    @Path("/login")
    @GET
    public LoginView login() {
        return new LoginView();
    }

    @Path("/start_authentication")
    @POST
    public String startAuthentication(@FormParam("username") String username, @FormParam("password") String password) {
        boolean validLogin = database.validateLogin(username, password);
        if (!validLogin) throw new InvalidLoginException();

        return "fu";
    }
}
