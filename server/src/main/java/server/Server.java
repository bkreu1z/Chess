package server;

import Handlers.ClearHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        ClearHandler clearHandler = new ClearHandler();
        RegisterHandler

        // Register your endpoints and handle exceptions here.
        //Spark.post("/user", new RegisterHandler());
        //Spark.post("/session", new LoginHandler());
        //Spark.post("/game", new CreateGameHandler());
        //Spark.delete("/session", new DeleteGameHandler());
        Spark.delete("/db", clearHandler::handle);
        //Spark.get("/game", new ListGamesHandler());
        //Spark.put("/game", new JoinGameHandler());

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
