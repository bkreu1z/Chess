package server;

import Handlers.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        ClearHandler clearHandler = new ClearHandler();
        CreateGameHandler createGameHandler = new CreateGameHandler();
        JoinGameHandler joinGameHandler = new JoinGameHandler();
        ListGamesHandler listGamesHandler = new ListGamesHandler();
        LoginHandler loginHandler = new LoginHandler();
        LogoutHandler logoutHandler = new LogoutHandler();
        RegisterHandler registerHandler = new RegisterHandler();

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler::handle);
        Spark.post("/session", loginHandler::handle);
        Spark.post("/game", createGameHandler::handle);
        Spark.get("/game", listGamesHandler::handle);
        Spark.put("/game", joinGameHandler::handle);
        Spark.delete("/session", logoutHandler::handle);
        Spark.delete("/db", clearHandler::handle);

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
