package db;

import api.UserData;

import java.util.HashMap;

/**
 * Created by MGSchmidt on 7/28/2016.
 */
public class ResourceDatabaseMock implements ResourceDatabase {

    private HashMap<String, UserData> resourceDatabase = new HashMap<String, UserData>();

    public ResourceDatabaseMock() {
        resourceDatabase.put("Jens", new UserData("Jens Holdam", "10-09-1992", "male"));
        resourceDatabase.put("Mathias", new UserData("Mathias Schmidt", "19-06-1992", "female"));
    }

    public UserData getData(String userID) {
        return resourceDatabase.get(userID);
    }

}
