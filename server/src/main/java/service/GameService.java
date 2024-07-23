package service;

import Requests.CreateRequest;
import Responses.CreateResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

public class GameService {
    GameDAO gameDAO = new GameDAO();
    AuthDAO authDAO = new AuthDAO();

    public CreateResult createGame(CreateRequest request) throws Exception {
        if (authDAO.getAuth(request.authToken())) {
            Integer gameID = gameDAO.createGame(request.gameName());
            return new CreateResult(gameID.toString());
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
