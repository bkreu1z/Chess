package handlers;

import requests.LogoutRequest;
import responses.LogoutResult;
import responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));
        if (logoutRequest.authToken() == null) {
            response.status(500);
            Message message = new Message("Error: no authorization provided");
            return GSON.toJson(message);
        }
        try {
            LogoutResult logoutResult = USER_SERVICE.logoutUser(logoutRequest);
            response.status(200);
            return GSON.toJson(logoutResult);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return GSON.toJson(message);
        }
    }
}
