package service;

import Requests.CreateRequest;
import Requests.ListRequest;
import Responses.CreateResult;
import Responses.ListResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Set;

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

    public ListResult listGames(ListRequest request) throws Exception {
        if (authDAO.getAuth(request.authToken())) {
            Set<GameData> games = gameDAO.getAllGames();
            return new ListResult(games);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
