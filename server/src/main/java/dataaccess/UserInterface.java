package dataaccess;

public interface UserInterface {
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

    boolean createUser(String username, String password, String email);

    boolean getUser(String username);

    boolean verifyPassword(String username, String password);

    boolean deleteUser(String username);

    void clear();
}
