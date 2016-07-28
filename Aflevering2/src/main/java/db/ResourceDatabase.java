package db;

import api.UserData;

/**
 * Created by MGSchmidt on 7/28/2016.
 */
public interface ResourceDatabase {
    UserData getData(String userID);
}
