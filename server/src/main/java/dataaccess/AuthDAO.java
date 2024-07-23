package dataaccess;

import model.AuthData;

import java.util.UUID;

public class AuthDAO implements AuthInterface{
    @Override
        public String createAuth(String username) {
            String token = UUID.randomUUID().toString();
            memoryAuthDAO.addAuthData(new AuthData(token, username));
            return token;
        }

    @Override
        public boolean getAuth(String token) {
            return memoryAuthDAO.isInMemory(token);
    }

    @Override
        public boolean deleteAuth(String token) {
            return memoryAuthDAO.deleteAuth(token);
    }

    @Override
        public void clear() {
            memoryAuthDAO.clear();
    }
}
