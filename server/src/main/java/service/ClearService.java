package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    GameDAO gameDAO = new GameDAO();
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();

    public void clear() {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();}
}
