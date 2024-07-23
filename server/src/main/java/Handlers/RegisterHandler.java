package Handlers;

import Requests.RegisterRequest;
import Responses.Message;
import Responses.RegisterResult;
import dataaccess.DataAccessException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class RegisterHandler implements Route {
    UserService userService = new UserService();
    Gson gson = new Gson();

    public Object handle(Request request, Response response) throws Exception {
        RegisterRequest register = gson.fromJson(request.body(), RegisterRequest.class);
        if (register.username() == null || register.password() == null || register.email() == null) {
            response.status(400);
            Message message = new Message("Error: bad request");
            return gson.toJson(message);
        }
        try {
            RegisterResult result = userService.registerUser(register);
            response.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            response.status(403);
            Message message = new Message(e.getMessage());
            return gson.toJson(message);
        }
    }
}
