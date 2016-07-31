package db;

import api.AccessToken;
import resources.AuthorizationServerResource;

import java.security.SecureRandom;
import java.util.HashMap;

public class AuthorizationDatabaseMock implements AuthorizationDatabase {
    private final int DEFAULT_EXPIRATION_TIME = 3600000;

    private HashMap<String, String> userDatabase = new HashMap<String, String>();
    private HashMap<Integer, AuthorizationData> authzCodeDatabase = new HashMap<Integer, AuthorizationData>();
    private HashMap<Integer, AccessTokenData> accessTokenDatabase = new HashMap<Integer, AccessTokenData>();
    private HashMap<Integer, Integer> clientSecretDatabase = new HashMap<Integer, Integer>();

    public AuthorizationDatabaseMock() {
        userDatabase.put("Jens", "123456");
        userDatabase.put("Mathias", "654321");
        userDatabase.put("woopwoop", "123");
        clientSecretDatabase.put(1337, 7331);
    }

    public boolean validateUserPassword(String username, String password) {
        String passwordForUser = userDatabase.get(username);
        return passwordForUser.equals(password);
    }

    public void storeAuthorizationCodeForClient(int code, int clientID, String userID, long createdTime, String redirectURI) {
        authzCodeDatabase.put(code, new AuthorizationData(createdTime, redirectURI, clientID, userID));
    }

    public boolean validateCodeForClient(int code, int clientID, String redirectURI) {
        AuthorizationData authorizationData = authzCodeDatabase.get(code);
        if (authorizationData == null) return false;

        // Ensure that code is fresh, here 10 minutes
        if (authorizationData.getCreatedTime() + 600000 < System.currentTimeMillis()) return false;

        // Ensure same redirect ID
        if (!redirectURI.equals(authorizationData.getRedirectURI())) return false;

        // Ensure same client ID
        if (clientID != authorizationData.getClientID()) return false;

        return true;
    }

    public AccessToken createAndStoreAccessToken(int code) {
        AuthorizationData authorizationData = authzCodeDatabase.get(code);

        SecureRandom random = new SecureRandom();
        int accessToken = random.nextInt();
        accessToken = (code < 0) ? code * -1 : code;

        AccessTokenData accessTokenData = new AccessTokenData(authorizationData.getClientID(), authorizationData.getUserID(), System.currentTimeMillis(), DEFAULT_EXPIRATION_TIME);
        accessTokenDatabase.put(accessToken, accessTokenData);
        return new AccessToken(accessToken, AccessToken.TokenType.BEARER_TOKEN);
    }

    public void deleteAuthorizationCode(int code) {
        authzCodeDatabase.remove(code);
    }

    public boolean validateTokenForScope(AuthorizationServerResource.Scope scope, int accessToken) {
        //test whether accestoken is in database, and hasn't expired
        long currentTime = System.currentTimeMillis();
        AccessTokenData tokenData = accessTokenDatabase.get(accessToken);
        if (tokenData == null) return false;
        if (currentTime > tokenData.getCreatedTime() + tokenData.getExpirationTime()) return false;
        return true;
    }

    public String getUserFromToken(int accessToken){
        return accessTokenDatabase.get(accessToken).userID;
    }

    public boolean validateClient(int clientID, int secret) {
        return clientSecretDatabase.get(clientID).equals(secret);
    }


    private class AccessTokenData {
        private int clientID;
        private String userID;
        private long createdTime;
        private int expirationTime;

        AccessTokenData(int clientID, String userID, long createdTime, int expirationTime) {
            this.clientID = clientID;
            this.userID = userID;
            this.createdTime = createdTime;
            this.expirationTime = expirationTime;
        }

        public int getClientID() {
            return clientID;
        }

        public String getUserID() {
            return userID;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public int getExpirationTime() {
            return expirationTime;
        }
    }

    private class AuthorizationData {
        private long createdTime;
        private String redirectURI;
        private int clientID;
        private String userID;

        AuthorizationData(long createdTime, String redirectURI, int clientID, String userID) {
            this.createdTime = createdTime;
            this.redirectURI = redirectURI;
            this.clientID = clientID;
            this.userID = userID;
        }

        public int getClientID() {
            return clientID;
        }

        public String getUserID() {
            return userID;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public String getRedirectURI() {
            return redirectURI;
        }
    }
}
