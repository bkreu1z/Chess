package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Set;

public class MemoryUserDAO {
    Set<UserData> users;

    public MemoryUserDAO() {
        users = new HashSet<>();
    }

    public void addUser(UserData user) {
        users.add(user);
    }

    public boolean getUser(String user) {
        if (users == null) {
            return false;
        }
        for (UserData userData : users) {
            if (userData.username().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public boolean verifyPassword(String username, String password) {
        if (users == null) {
            return false;
        }
        for (UserData userData : users) {
            if (userData.username().equals(username) && userData.password().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String username) {
        if (users != null) {
            for (UserData userData : users) {
                if (userData.username().equals(username)) {
                    users.remove(userData);
                    return true;
                }
            }
        }
        return false;
    }

    public void clear() {
        if (users != null) {
            users.clear();
        }
    }
}
