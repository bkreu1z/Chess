package Handlers;

import Requests.JoinRequest;
import Responses.JoinResult;
import Responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        JoinRequest joinRequest = gson.fromJson(request.body(), JoinRequest.class);
        joinRequest = new JoinRequest(request.headers("authorization"), joinRequest.playerColor(), joinRequest.gameID());
        if (joinRequest.authToken() == null || joinRequest.playerColor() == null || joinRequest.gameID() == null) {
            response.status(400);
            Message message = new Message("Error: bad request");
            return gson.toJson(message);
        }
        try {
            JoinResult joinResult = gameService.joinGame(joinRequest);
            response.status(200);
            return gson.toJson(joinResult);
        } catch (DataAccessException e) {
            Message message = new Message(e.getMessage());
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
            }
            else if (e.getMessage().equals("Error: already taken")) {
                response.status(403);
            }
            return gson.toJson(message);
        }
    }
}
