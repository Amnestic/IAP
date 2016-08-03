package resources;

import api.AccessToken;
import api.UserData;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/client")
public class ClientResource {

    private int clientID;
    private int secret;
    private String host;

    public ClientResource(int clientID, int secret, String host) {
        this.clientID = clientID;
        this.secret = secret;
        this.host = host;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/success")
    public String success(@NotNull @QueryParam("code") int code) {
        // Get access token
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(host + ":8080").path("authorization/access_token_request");
        Form form = new Form();
        form.param("grant_type", "code");
        form.param("code", String.valueOf(code));
        form.param("redirect_uri", host + ":8080/client/success");
        form.param("client_id", String.valueOf(clientID));
        form.param("secret", String.valueOf(secret));
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        AccessToken accessToken = response.readEntity(AccessToken.class);

        // request resource?
        target = client.target(host + ":8080").path("resource/request_public_info").queryParam("access_token", accessToken.getAccessToken());

        response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        UserData data = response.readEntity(UserData.class);

        return "Name: " + data.getName() + ", Birthday: " + data.getBirthday() + ", Gender: " + data.getGender();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String index() {
        return "<a href=\"" + host + ":8080/authorization/request_authz?response_type=code&client_id=1337&redirect_uri=" + host + ":8080/client/success\">Klik her for at authorize 1337!</a>";
    }
}
