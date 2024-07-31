package dataaccess;

public interface AuthInterface {
    MemoryAuthDAO MEMORY_AUTH_DAO = new MemoryAuthDAO();

    String createAuth(String username) throws DataAccessException;

    boolean getAuth(String token) throws DataAccessException;

    String getUsername(String token) throws DataAccessException;

    boolean deleteAuth(String token);

    void clear();
}
