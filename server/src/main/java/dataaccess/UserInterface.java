package dataaccess;

import model.UserData;

import java.util.Set;

public interface UserInterface {
    MemoryUserDAO MEMORY_USER_DAO = new MemoryUserDAO();

    boolean createUser(String username, String password, String email);

    boolean getUser(String username);

    Set<UserData> getUsers();

    boolean verifyPassword(String username, String password);

    boolean deleteUser(String username);

    void clear();
}
