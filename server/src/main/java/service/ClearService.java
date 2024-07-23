package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import Responses.ClearResult;

public class ClearService {
    GameDAO gameDAO = new GameDAO();
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();

    public ClearResult clear() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
        return new ClearResult();
    }
}
