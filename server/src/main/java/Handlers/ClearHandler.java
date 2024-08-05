package handlers;

import responses.Message;
import dataaccess.DataAccessException;

import responses.ClearResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route, HandlerInterface {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String message = "Cleared Database Successfully";
            ClearResult result = CLEAR_SERVICE.clear();
            response.status(200);
            return GSON.toJson(result);
        } catch (DataAccessException e) {
            response.status(500);
            Message message = new Message(e.getMessage());
            return GSON.toJson(message);
        }
    }
}
