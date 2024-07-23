package Handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;

import Responses.ClearResult;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    ClearService clearService = new ClearService();
    Gson gson = new Gson();

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
            String message = e.getMessage();
            return gson.toJson(message);
        }
    }
}
