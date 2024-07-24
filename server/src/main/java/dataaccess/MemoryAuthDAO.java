package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Set;

public class MemoryAuthDAO {
    Set<AuthData> authDataSet;

    public MemoryAuthDAO() {
        authDataSet = new HashSet<>();
    }

    public void addAuthData(AuthData authData) {
        if (authDataSet != null) {
            authDataSet.add(authData);
        }
    }

    public boolean isInMemory(String token) {
        if (authDataSet == null) {
            return false;
        }
        for (AuthData authData : authDataSet) {
            if (authData.authToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    public String getUsername(String token) {
        if (authDataSet != null) {
            for (AuthData authData : authDataSet) {
                if (authData.authToken().equals(token)) {
                    return authData.username();
                }
            }
        }
        return null;
    }

    public boolean deleteAuth(String token) {
        if (authDataSet != null) {
            for (AuthData authData : authDataSet) {
                if (authData.authToken().equals(token)) {
                    authDataSet.remove(authData);
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        if (authDataSet != null) {
            authDataSet.clear();
        }
    }
}
