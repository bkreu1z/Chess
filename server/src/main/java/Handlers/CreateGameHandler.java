package handlers;

import requests.CreateRequest;
import responses.CreateResult;
import responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        CreateRequest createRequest = GSON.fromJson(request.body(), CreateRequest.class);
        createRequest = new CreateRequest(request.headers("authorization"), createRequest.gameName());
        if (createRequest.authToken() == null || createRequest.gameName() == null) {
            response.status(400);
            Message message = new Message("Error: bad request");
            return GSON.toJson(message);
        }
        try {
            CreateResult createResult = GAME_SERVICE.createGame(createRequest);
            response.status(200);
            return GSON.toJson(createResult);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return GSON.toJson(message);
        }
    }
}
