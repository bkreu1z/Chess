package service;

import requests.CreateRequest;
import requests.JoinRequest;
import requests.ListRequest;
import responses.CreateResult;
import responses.JoinResult;
import responses.ListResult;
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
            String gameID = gameDAO.createGame(request.gameName());
            return new CreateResult(gameID);
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

    public JoinResult joinGame(JoinRequest request) throws Exception {
        if (authDAO.getAuth(request.authToken())) {
            String username = authDAO.getUsername(request.authToken());
            if (!gameDAO.joinGame(username, request.playerColor(), request.gameID())) {
                throw new DataAccessException("Error: already taken");
            }
            return new JoinResult();
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
