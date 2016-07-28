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

    public ClientResource(int clientID) {
        this.clientID = clientID;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/success")
    public String success(@NotNull @QueryParam("code") int code) {
        // Get access token
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080").path("authorization/access_token_request");
        Form form = new Form();
        form.param("grant_type", "code");
        form.param("code", String.valueOf(code));
        form.param("redirect_uri", "localhost:8080/client/success");
        form.param("client_id", String.valueOf(clientID));
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        AccessToken accessToken = response.readEntity(AccessToken.class);

        // request resource
        WebTarget resourceTarget = client.target("http://localhost:8080").path("resource/request_public_info?access_token=" + accessToken);
        Response resourceResponse = resourceTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        UserData data = resourceResponse.readEntity(UserData.class);
        return "Name: " + data.getName() + "<br/> Birthday: " + data.getBirthday() + "<br/> Gender: " + data.getGender();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String index() {
        // TODO
        return null;
    }
}
