package handlers;

import requests.ListRequest;
import responses.ListResult;
import responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        ListRequest listRequest = new ListRequest(request.headers("authorization"));
        try {
            ListResult listResult = GAME_SERVICE.listGames(listRequest);
            response.status(200);
            return GSON.toJson(listResult);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return GSON.toJson(message);
        }
    }
}
