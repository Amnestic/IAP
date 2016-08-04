package resources;

import com.yubico.u2f.data.DeviceRegistration;
import com.yubico.u2f.data.messages.AuthenticateRequestData;
import com.yubico.u2f.data.messages.AuthenticateResponse;
import com.yubico.u2f.data.messages.RegisterRequestData;
import com.yubico.u2f.data.messages.RegisterResponse;
import com.yubico.u2f.exceptions.DeviceCompromisedException;
import com.yubico.u2f.exceptions.NoEligibleDevicesException;
import db.Database;
import exceptions.InvalidLoginException;
import views.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.yubico.u2f.U2F;

@Path("/auth")
public class SecurityKeyResource {
    private String APP_ID;
    private Database database;
    private U2F u2f = U2F.withoutAppIdValidation();

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
        database.insertRegChallenge(username, challenge);
        return new RegisterSecurityKeyView(username, challenge.toJson(), APP_ID);
    }

    @Path("/finish_registration")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String finishRegistration(@FormParam("username") String username, @FormParam("response-input") String responseInput) {
        RegisterRequestData challenge = database.getRegistrationChallenge(username);
        database.deleteRegistrationChallenge(username);
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
    public AuthenticateSecurityKeyView startAuthentication(@FormParam("username") String username, @FormParam("password") String password) throws NoEligibleDevicesException {
        boolean validLogin = database.validateLogin(username, password);
        if (!validLogin) throw new InvalidLoginException();
        Iterable<DeviceRegistration> devices = database.getDevicesForUser(username);
        AuthenticateRequestData challenge = u2f.startAuthentication(APP_ID, devices);
        database.insertAuthChallenge(username, challenge);
        return new AuthenticateSecurityKeyView(username, challenge.toJson(), APP_ID);
    }

    @Path("/finish_authentication")
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String finishAuthentication(@FormParam("username") String username, @FormParam("response-input") String responseInput) throws DeviceCompromisedException {
        //check signature
        AuthenticateRequestData challenge = database.getAuthenticationChallenge(username);
        AuthenticateResponse response = AuthenticateResponse.fromJson(responseInput);
        database.deleteAuthenticationChallenge(username);
        DeviceRegistration device = u2f.finishAuthentication(challenge, response, database.getDevicesForUser(username));
        return "Success, you have logged in as " + username + "! the counter was: " + device.getCounter();
    }
}
