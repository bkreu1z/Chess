package dataaccess;

import model.AuthData;

import java.util.UUID;

public class AuthDAO implements AuthInterface{
    @Override
    public String createAuth(String username) {
        String token = UUID.randomUUID().toString();
        MEMORY_AUTH_DAO.addAuthData(new AuthData(token, username));
        return token;
    }

    @Override
    public boolean getAuth(String token) {
        return MEMORY_AUTH_DAO.isInMemory(token);
    }

    @Override
    public String getUsername(String token) {
        return MEMORY_AUTH_DAO.getUsername(token);
    }

    @Override
    public boolean deleteAuth(String token) {
        return MEMORY_AUTH_DAO.deleteAuth(token);
    }

    @Override
    public void clear() {
        MEMORY_AUTH_DAO.clear();
    }
}
