package dataaccess;

import model.AuthData;

public interface AuthInterface {
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

    String createAuth(String username);

    boolean getAuth(String token);

    boolean deleteAuth(String token);

    void clear();
}
