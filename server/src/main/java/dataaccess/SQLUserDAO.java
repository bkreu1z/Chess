package dataaccess;

import model.UserData;

import java.util.Set;

public class SQLUserDAO implements UserInterface {
    @Override
    public boolean createUser(String username, String password, String email) {
        return false;
    }

    @Override
    public boolean getUser(String username) {
        return false;
    }

    @Override
    public Set<UserData> getUsers() {
        return Set.of();
    }

    @Override
    public boolean verifyPassword(String username, String password) {
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return false;
    }

    @Override
    public void clear() {

    }
}
