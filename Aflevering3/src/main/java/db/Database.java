package db;

import com.yubico.u2f.data.DeviceRegistration;
import com.yubico.u2f.data.messages.RegisterRequestData;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private HashMap<String, String> users;
    private HashMap<String, ArrayList<DeviceRegistration>> registeredDevices;
    private HashMap<String, RegisterRequestData> challengeStore;

    public Database() {
        this.users = new HashMap<String, String>();
        this.registeredDevices = new HashMap<String, ArrayList<DeviceRegistration>>();
        this.challengeStore = new HashMap<String, RegisterRequestData>();
    }

    public void insertUser(String username, String password) {
        users.put(username, password);
    }

    public Iterable getDevicesForUser(String username) {
        ArrayList<DeviceRegistration> devicesForUser = registeredDevices.get(username);
        if (devicesForUser == null) {
            devicesForUser = new ArrayList<DeviceRegistration>();
            registeredDevices.put(username, devicesForUser);
        }

        return devicesForUser;
    }

    public void insertDeviceForUser(String username, DeviceRegistration device) {
        ArrayList<DeviceRegistration> devicesForUser = registeredDevices.get(username);
        if (devicesForUser == null) {
            devicesForUser = new ArrayList<DeviceRegistration>();
            registeredDevices.put(username, devicesForUser);
        }

        devicesForUser.add(device);
    }

    public void insertChallenge(String username, RegisterRequestData challenge) {
        challengeStore.put(username, challenge);
    }

    public RegisterRequestData findChallenge(String username) {
        return challengeStore.get(username);
    }

    public void deleteChallenge(String username) {
        challengeStore.remove(username);
    }

    public boolean validateLogin(String username, String password) {
        String correctPassword = users.get(username);
        return correctPassword.equals(password);
    }
}
