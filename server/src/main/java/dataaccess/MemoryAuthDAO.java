package dataaccess;

import model.AuthData;

import java.util.Set;

public class MemoryAuthDAO {
    Set<AuthData> authDataSet;

    public MemoryAuthDAO() {}

    public void addAuthData(AuthData authData) {
        authDataSet.add(authData);
    }

    public boolean isInMemory(String token) {
        for (AuthData authData : authDataSet) {
            if (authData.authToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteAuth(String token) {
        for (AuthData authData : authDataSet) {
            if (authData.authToken().equals(token)) {
                authDataSet.remove(authData);
                return true;
            }
        }
        return false;
    }

    public void clear() {
        authDataSet.clear();
    }
}
