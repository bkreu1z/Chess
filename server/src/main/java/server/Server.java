package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import handlers.*;
import server.websocket.WebSocketHandler;
import spark.*;

import java.sql.SQLException;

public class Server {
    private final WebSocketHandler webSocketHandler;
    private final ClearHandler clearHandler;
    private final CreateGameHandler createGameHandler;
    private final JoinGameHandler joinGameHandler;
    private final ListGamesHandler listGamesHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final RegisterHandler registerHandler;

    public Server() {
        webSocketHandler = new WebSocketHandler();
        clearHandler = new ClearHandler();
        createGameHandler = new CreateGameHandler();
        joinGameHandler = new JoinGameHandler();
        listGamesHandler = new ListGamesHandler();
        loginHandler = new LoginHandler();
        logoutHandler = new LogoutHandler();
        registerHandler = new RegisterHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            configureDatabase();
        } catch (Exception e) {
            System.out.println(e);
        }

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.delete("/session", this::logout);
        Spark.delete("/db", this::clear);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) throws Exception {
        return registerHandler.handle(req, res);
    }

    private Object login(Request req, Response res) throws Exception {
        return loginHandler.handle(req, res);
    }

    private Object createGame(Request req, Response res) throws Exception {
        return createGameHandler.handle(req, res);
    }

    private Object listGames(Request req, Response res) throws Exception {
        return listGamesHandler.handle(req, res);
    }

    private Object joinGame(Request req, Response res) throws Exception {
        return joinGameHandler.handle(req, res);
    }

    private Object logout(Request req, Response res) throws Exception {
        return logoutHandler.handle(req, res);
    }

    private Object clear(Request req, Response res) throws Exception {
        return clearHandler.handle(req, res);
    }

    private final String[] createStatements = {
            """
CREATE TABLE IF NOT EXISTS users (
 `id` int NOT NULL AUTO_INCREMENT,
 `username` varchar(255) NOT NULL,
 `password` varchar(255) NOT NULL,
 `email` varchar(255) NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
""",
            """

CREATE TABLE IF NOT EXISTS auth (
 `id` int NOT NULL AUTO_INCREMENT,
 `token` varchar(255) NOT NULL,
 `authusername` varchar(255) NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
""",
            """

CREATE TABLE IF NOT EXISTS games (
 `id` int NOT NULL AUTO_INCREMENT,
 `whiteUsername` varchar(255) NULL,
 `blackUsername` varchar(255) NULL,
 `gameName` varchar(255) NOT NULL,
 `game` varchar(3000) NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
"""
    };

    public void configureDatabase() throws SQLException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            System.out.println("didn't like create database");
        }
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException e) {
            System.out.println("didn't like create statements");
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
