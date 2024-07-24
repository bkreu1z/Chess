package dataaccess;

import model.UserData;

import java.util.Set;

public class UserDAO implements UserInterface{
    @Override
    public boolean createUser(String username, String password, String email) {
        if (!getUser(username)) {
            MEMORY_USER_DAO.addUser(new UserData(username, password, email));
            return true;
        }
        return false;
    }

    @Override
    public boolean getUser(String username) {
        return MEMORY_USER_DAO.getUser(username);
    }

    @Override
    public Set<UserData> getUsers() {
        return MEMORY_USER_DAO.getUsers();
    }

    @Override
    public boolean verifyPassword(String username, String password) {
        return MEMORY_USER_DAO.verifyPassword(username, password);
    }

    @Override
    public boolean deleteUser(String username) {
        return MEMORY_USER_DAO.deleteUser(username);
    }

    @Override
    public void clear() {
        MEMORY_USER_DAO.clear();
    }
}
