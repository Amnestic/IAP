package db;

import api.AccessToken;
import resources.AuthorizationServerResource;

public interface AuthorizationDatabase {
    boolean validateUserPassword(String username, String password);

    void storeAuthorizationCodeForClient(int code, int clientID, String userID, long createdTime, String redirectURI);

    boolean validateCodeForClient(int code, int clientID, String redirectURI);

    AccessToken createAndStoreAccessToken(int code);

    void deleteAuthorizationCode(int code);

    boolean validateTokenForScope(AuthorizationServerResource.Scope scope, int accessToken);

    String getUserFromToken(int accessToken);
}
