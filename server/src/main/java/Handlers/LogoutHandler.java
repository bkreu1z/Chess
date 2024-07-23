package Handlers;

import Requests.LogoutRequest;
import Responses.LogoutResult;
import Responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        //LogoutRequest logoutRequest = gson.fromJson(request.headers()[0], LogoutRequest.class);
        LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));
        if (logoutRequest.authToken() == null) {
            response.status(500);
            Message message = new Message("Error: no authorization provided");
            return gson.toJson(message);
        }
        try {
            LogoutResult logoutResult = userService.logoutUser(logoutRequest);
            response.status(200);
            return gson.toJson(logoutResult);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return gson.toJson(message);
        }
    }
}
