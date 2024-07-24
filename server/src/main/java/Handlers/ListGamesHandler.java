package Handlers;

import Requests.ListRequest;
import Responses.ListResult;
import Responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        ListRequest listRequest = new ListRequest(request.headers("authorization"));
        try {
            ListResult listResult = gameService.listGames(listRequest);
            response.status(200);
            return gson.toJson(listResult);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return gson.toJson(message);
        }
    }
}
