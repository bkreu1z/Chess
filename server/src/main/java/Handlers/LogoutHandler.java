package Handlers;

import Requests.LogoutRequest;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route, HandlerInterface {
    public Object handle(Request request, Response response) throws Exception {
        LogoutRequest logoutRequest = gson.fromJson(request.body(), LogoutRequest.class);
    }
}
