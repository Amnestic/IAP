package api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MGSchmidt on 7/28/2016.
 */
public class UserData {
    private String name;
    private String birthday;
    private String gender;

    public UserData() {

    }

    public UserData(String name, String birthday, String gender) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getBirthday() {
        return birthday;
    }

    @JsonProperty
    public String getGender() {
        return gender;
    }
}