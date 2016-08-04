package db;

import com.yubico.u2f.data.DeviceRegistration;
import com.yubico.u2f.data.messages.AuthenticateRequestData;
import com.yubico.u2f.data.messages.RegisterRequestData;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private HashMap<String, String> users;
    private HashMap<String, ArrayList<DeviceRegistration>> registeredDevices;
    private HashMap<String, RegisterRequestData> registrationChallengeStore;
    private HashMap<String, AuthenticateRequestData> authenticationChallengeStore;

    public Database() {
        this.users = new HashMap<String, String>();
        this.registeredDevices = new HashMap<String, ArrayList<DeviceRegistration>>();
        this.registrationChallengeStore = new HashMap<String, RegisterRequestData>();
        this.authenticationChallengeStore = new HashMap<String, AuthenticateRequestData>();
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

    public void insertAuthChallenge(String username, AuthenticateRequestData challenge) {
        authenticationChallengeStore.put(username, challenge);
    }

    public void insertRegChallenge(String username, RegisterRequestData challenge) {
        registrationChallengeStore.put(username, challenge);
    }

    public RegisterRequestData getRegistrationChallenge(String username) {
        return registrationChallengeStore.get(username);
    }

    public AuthenticateRequestData getAuthenticationChallenge(String username) {
        return authenticationChallengeStore.get(username);
    }

    public void deleteRegistrationChallenge(String username) {
        registrationChallengeStore.remove(username);
    }

    public void deleteAuthenticationChallenge(String username) {
        authenticationChallengeStore.remove(username);
    }

    public boolean validateLogin(String username, String password) {
        String correctPassword = users.get(username);
        return correctPassword.equals(password);
    }
}
