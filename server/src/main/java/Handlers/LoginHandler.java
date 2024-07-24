package handlers;

import requests.LoginRequest;
import responses.LoginResult;
import responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        LoginRequest loginRequest = GSON.fromJson(request.body(), LoginRequest.class);
        if (loginRequest.username() == null || loginRequest.password() == null) {
            response.status(500);
            Message message = new Message("Error: username or password is missing");
            return GSON.toJson(message);
        }
        try {
            LoginResult result = USER_SERVICE.loginUser(loginRequest);
            response.status(200);
            return GSON.toJson(result);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return GSON.toJson(message);
        }
    }
}
