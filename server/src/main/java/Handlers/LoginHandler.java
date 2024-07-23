package Handlers;

import Requests.LoginRequest;
import Responses.LoginResult;
import Responses.Message;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        if (loginRequest.username() == null || loginRequest.password() == null) {
            response.status(500);
            Message message = new Message("Error: username or password is missing");
            return gson.toJson(message);
        }
        try {
            LoginResult result = userService.loginUser(loginRequest);
            response.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            response.status(401);
            Message message = new Message(e.getMessage());
            return gson.toJson(message);
        }
    }
}
