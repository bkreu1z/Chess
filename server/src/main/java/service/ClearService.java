package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import Responses.ClearResult;

public class ClearService {
    GameDAO gameDAO = new GameDAO();
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();

    public ClearResult clear() {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
        return new ClearResult();
    }
}
