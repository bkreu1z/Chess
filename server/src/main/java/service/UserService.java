package service;

import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.LoginResult;
import responses.LogoutResult;
import responses.RegisterResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

public class UserService {
    AuthDAO authDAO = new AuthDAO();
    UserDAO userDAO = new UserDAO();

    public RegisterResult registerUser(RegisterRequest request) throws DataAccessException {
        if (!userDAO.createUser(request.username(), request.password(), request.email())) {
            throw new DataAccessException("Error: already taken");
        }
        String token = authDAO.createAuth(request.username());
        return new RegisterResult(request.username(), token);
    }

    public LoginResult loginUser(LoginRequest request) throws DataAccessException {
        if (userDAO.verifyPassword(request.username(), request.password())) {
            String token = authDAO.createAuth(request.username());
            return new LoginResult(request.username(), token);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public LogoutResult logoutUser(LogoutRequest request) throws DataAccessException {
        if (authDAO.getAuth(request.authToken())) {
            authDAO.deleteAuth(request.authToken());
            return new LogoutResult();
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
