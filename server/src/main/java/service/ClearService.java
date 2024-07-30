package service;

import dataaccess.*;
import responses.ClearResult;

public class ClearService {
    GameInterface gameDAO = new SQLGameDAO();
    UserInterface userDAO = new SQLUserDAO();
    AuthInterface authDAO = new SQLAuthDAO();

    public ClearResult clear() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
        return new ClearResult();
    }
}
