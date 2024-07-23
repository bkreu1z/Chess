package Handlers;

import Responses.Message;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;

import Responses.ClearResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route, HandlerInterface {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //deserialize here
        //make request object
        //pass request object service
        //service to response
        //reserialize response object
        try {
            String message = "Cleared Database Successfully";
            ClearResult result = clearService.clear();
            response.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            response.status(500);
            Message message = new Message(e.getMessage());
            return gson.toJson(message);
        }
    }
}
