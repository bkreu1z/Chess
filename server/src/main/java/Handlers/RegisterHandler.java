package handlers;

import requests.RegisterRequest;
import responses.Message;
import responses.RegisterResult;
import dataaccess.DataAccessException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class RegisterHandler implements Route, HandlerInterface{

    public Object handle(Request request, Response response) throws Exception {
        RegisterRequest register = GSON.fromJson(request.body(), RegisterRequest.class);
        if (register.username() == null || register.password() == null || register.email() == null) {
            response.status(400);
            Message message = new Message("Error: bad request");
            return GSON.toJson(message);
        }
        try {
            RegisterResult result = USER_SERVICE.registerUser(register);
            response.status(200);
            return GSON.toJson(result);
        } catch (DataAccessException e) {
            response.status(403);
            Message message = new Message(e.getMessage());
            return GSON.toJson(message);
        }
    }
}
