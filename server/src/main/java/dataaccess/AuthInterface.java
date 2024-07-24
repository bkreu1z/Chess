package dataaccess;

public interface AuthInterface {
    MemoryAuthDAO MEMORY_AUTH_DAO = new MemoryAuthDAO();

    String createAuth(String username);

    boolean getAuth(String token);

    String getUsername(String token);

    boolean deleteAuth(String token);

    void clear();
}
