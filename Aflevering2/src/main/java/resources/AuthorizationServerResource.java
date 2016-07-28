package resources;


import api.AccessToken;
import api.TokenValidation;
import db.Database;
import exceptions.AuthorizationDeniedException;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.rmi.AccessException;
import java.security.SecureRandom;
import java.util.Optional;

@Path("/authorization")
@Produces(MediaType.APPLICATION_JSON)
public class AuthorizationServerResource {

    private Database database;

    public AuthorizationServerResource(Database database) {
        this.database = database;
    }

    // State ignored here
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/request_authz")
    public String authorize(@NotNull @QueryParam("client_id") String clientID, @NotNull @QueryParam("redirect_uri") String redirectURI,
                            @QueryParam("scope") Optional<String> scope, @NotNull @QueryParam("response_type") String responseType) {

        if (!responseType.equals("code")) throw new AuthorizationDeniedException();

        // TODO Lookup client for giving better name
        // TODO validate scope
        // String[] scope


        // Lmao this ugly as fuck
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Login to authorize the client</title>");
        html.append("<body>");
        html.append("<h3>" + clientID + " would like your access to:</h3>");
        html.append("<ul>");
        html.append("<li>Public information</li>");
        html.append("</ul>");
        html.append("<h4>Logging in authorizes " + clientID + " to these items</h4>");
        html.append("<form action=\"./login_and_grant_auth\" method=\"post\">");
        html.append("<input type=\"text\" name=\"username\" placeholder=\"username\" required>");
        html.append("<input type=\"password\" name=\"password\" placeholder=\"password\" required>");
        html.append("<input type=\"hidden\" name=\"client_id\" value=\"" + clientID + "\">");
        html.append("<input type=\"hidden\" name=\"redirect_uri\" value=\"" + redirectURI + "\">");
        html.append("<input type=\"hidden\" name=\"scope\" value=\"" + scope + "\">");
        html.append("<input type=\"hidden\" name=\"response_type\" value=\""+ responseType +"\">");
        html.append("<input type=\"submit\">");
        html.append("</form>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    @POST
    @Path("/login_and_grant_auth")
    public Response login(@NotNull @FormParam("client_id") int clientID, @NotNull @FormParam("redirect_uri") String redirectURI,
                          @FormParam("scope") Optional<String> scope, @NotNull @FormParam("response_type") String responseType,
                          @NotNull @FormParam("username") String username, @NotNull @FormParam("password") String password) {
        if (!database.validateUserPassword(username, password)) throw new AuthorizationDeniedException();
        if (!responseType.equals("code")) throw new AuthorizationDeniedException();

        // Generate code and store
        SecureRandom random = new SecureRandom();
        int code =  random.nextInt();
        code = (code < 0) ? code * -1 : code;
        database.storeAuthorizationCodeForClient(code, clientID, username, System.currentTimeMillis(), redirectURI);

        // Redirect to client - since we are on localhost, we need to supply the relative path.
        // TODO use redirect_uri
        return Response.seeOther(URI.create("/client/success?code=" + code)).build();
    }

    @POST
    @Path("/access_token_request")
    public AccessToken accessTokenRequest(@NotNull @FormParam("grant_type") String grantType, @NotNull @FormParam("code") int code,
                                          @NotNull @FormParam("redirect_uri") String redirectURI, @NotNull @FormParam("client_id") int clientID) {
        if (!grantType.equals("code")) throw new AuthorizationDeniedException();

        // TODO should couple code with its scope

        if (!database.validateCodeForClient(code, clientID, redirectURI)) throw new AuthorizationDeniedException();

        AccessToken accessToken = database.createAndStoreAccessToken(code);
        database.deleteAuthorizationCode(code);

        return accessToken;
    }


    @GET
    @Path("/validate_token")
    public TokenValidation validateToken(@QueryParam("access_token") int accessToken) {
        return (database.validateTokenForScope(Scope.ALL, accessToken)) ? new TokenValidation(true) : new TokenValidation(false);
    }


    public enum Scope {
        ALL
    }
    // http://localhost:8080/authorization/request_authz?response_type=code&client_id=1337&redirect_uri=localhost:8080/client/success
}