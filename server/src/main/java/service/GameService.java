package service;

import dataaccess.*;
import requests.CreateRequest;
import requests.JoinRequest;
import requests.ListRequest;
import responses.CreateResult;
import responses.JoinResult;
import responses.ListResult;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    GameInterface gameDAO = new SQLGameDAO();
    AuthInterface authDAO = new SQLAuthDAO();

    public CreateResult createGame(CreateRequest request) throws DataAccessException {
        if (authDAO.getAuth(request.authToken())) {
            String gameID = gameDAO.createGame(request.gameName());
            return new CreateResult(gameID);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public ListResult listGames(ListRequest request) throws DataAccessException {
        if (authDAO.getAuth(request.authToken())) {
            ArrayList<GameData> games = gameDAO.getAllGames();
            return new ListResult(games);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public JoinResult joinGame(JoinRequest request) throws DataAccessException {
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
