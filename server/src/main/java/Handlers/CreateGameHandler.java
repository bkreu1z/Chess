package Handlers;

import Requests.CreateRequest;
import Responses.CreateResult;
import Responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        CreateRequest createRequest = gson.fromJson(request.body(), CreateRequest.class);
        createRequest = new CreateRequest(request.headers("authorization"), createRequest.gameName());
        if (createRequest.authToken() == null || createRequest.gameName() == null) {
            response.status(400);
            Message message = new Message("Error: bad request");
            return gson.toJson(message);
        }
        try {
            CreateResult createResult = gameService.createGame(createRequest);
            response.status(200);
            return gson.toJson(createResult);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return gson.toJson(message);
        }
    }
}
