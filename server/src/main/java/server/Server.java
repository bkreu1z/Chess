package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import handlers.*;
import spark.*;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.clearDatabase;

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

        try {
            configureDatabase();
        } catch (Exception e) {
            System.out.println(e);
        }

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
