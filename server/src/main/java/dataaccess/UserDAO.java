package dataaccess;

import model.UserData;

import java.util.Set;

public class UserDAO implements UserInterface{
    @Override
    public boolean createUser(String username, String password, String email) {
        if (!getUser(username)) {
            memoryUserDAO.addUser(new UserData(username, password, email));
            return true;
        }
        return false;
    }

    @Override
    public boolean getUser(String username) {
        return memoryUserDAO.getUser(username);
    }

    @Override
    public Set<UserData> getUsers() {
        return memoryUserDAO.getUsers();
    }

    @Override
    public boolean verifyPassword(String username, String password) {
        return memoryUserDAO.verifyPassword(username, password);
    }

    @Override
    public boolean deleteUser(String username) {
        return memoryUserDAO.deleteUser(username);
    }

    @Override
    public void clear() {
        memoryUserDAO.clear();
    }
}
