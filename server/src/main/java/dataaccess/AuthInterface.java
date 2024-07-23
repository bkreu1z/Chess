package dataaccess;

import model.AuthData;

public interface AuthInterface {
    String createAuth(String username);
    boolean getAuth(String token);
    boolean deleteAuth(String token);
    void clear();
}
