package service;

import Requests.RegisterRequest;
import Responses.RegisterResult;
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
}
