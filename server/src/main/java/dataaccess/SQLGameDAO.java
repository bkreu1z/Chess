package dataaccess;

import chess.ChessGame;
import model.GameData;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SQLGameDAO implements GameInterface {
    Gson gson = new Gson();

    @Override
    public String createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        String gameJson = gson.toJson(game);
        String gameID = "";
        var createStatement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        try {
            SQLAuthDAO.executeUpdate(createStatement, gameName, gameJson);
        } catch (Exception e) {
            throw new DataAccessException("could not create game");
        }
        var idStatement = "SELECT id FROM games WHERE `gameName` = \"" + gameName + "\"";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(idStatement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        gameID = Integer.toString((rs.getInt("id")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not get gameID");
        }
        return gameID;
    }

    @Override
    public boolean getGameByName(String gameName) throws DataAccessException {
        String statement = "SELECT * FROM games WHERE gameName = \"" + gameName + "\"";
        return findGameHelper(statement);
    }

    public static boolean findGameHelper(String statement) {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    @Override
    public Set<GameData> getAllGames() throws DataAccessException {
        Set<GameData> games = new HashSet<>();
        var statement = "SELECT * FROM games";
        try (Connection conn = DatabaseManager.getConnection()) {try (var ps = conn.prepareStatement(statement)) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
                    games.add(new GameData(rs.getString("id"), rs.getString("whiteUsername"),
                            rs.getString("blackUsername"), rs.getString("gameName"), game));
                }
            }
        }
        } catch (Exception e) {
            throw new DataAccessException("Could not get users");
        }
        return games;
    }

    private boolean checkColorNull(String playerColor, String gameID) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE id = \"" + gameID + "\"";
        String color = null;
        switch (playerColor) {
            case "WHITE" -> color = "whiteUsername";
            case "BLACK" -> color = "blackUsername";
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(color) == null;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean joinGame(String username, String playerColor, String gameID) {
        try {
            if (!checkColorNull(playerColor, gameID)) {
                return false;
            }
        } catch (DataAccessException e) {
            return false;
        }
        String color = null;
        var statement = "SELECT * FROM games WHERE id = \"" + gameID + "\"";
        if (playerColor.equals("WHITE")) {
            statement = "UPDATE games SET whiteUsername = ? WHERE id = ?";
        }
        else if (playerColor.equals("BLACK")) {
            statement = "UPDATE games SET blackUsername = ? WHERE id = ?";
        }
        try {
            SQLAuthDAO.executeUpdate(statement, username, gameID);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteGame(String gameName) {
        var statement = "DELETE FROM games WHERE gameName = ?";
        try {
            SQLAuthDAO.executeUpdate(statement, gameName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        var statement = "DELETE FROM games";
        try {
            SQLAuthDAO.executeUpdate(statement);
        } catch (Exception e) {
            System.out.println("could not delete games");
        }

    }
}
