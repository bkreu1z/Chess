package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import model.GameData;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public ArrayList<GameData> getAllGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
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

    public ChessGame getGameByID(String gameID) throws DataAccessException {
        String statement = "SELECT * FROM games WHERE id = \"" + gameID + "\"";
        ChessGame game = null;
        try (Connection conn = DatabaseManager.getConnection()) {try (var ps = conn.prepareStatement(statement)) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    game = gson.fromJson(rs.getString("game"), ChessGame.class);
                }
            }
        }
        } catch (Exception e) {
            throw new DataAccessException("Could not get game");
        }
        return game;
    }

    public boolean checkColorNull(String playerColor, String gameID) throws DataAccessException {
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

    public void leaveGame(String gameID, String playerColor) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE id = \"" + gameID + "\"";
        if (playerColor.equals("WHITE")) {
            statement = "UPDATE games SET whiteUsername = ? WHERE id = ?";
        } else {
            statement = "UPDATE games SET blackUsername = ? WHERE id = ?";
        }
        SQLAuthDAO.executeUpdate(statement, null, gameID);
    }

    public String getPlayerColor(String gameID, String userName) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE id = \"" + gameID + "\"";
        String whiteUsername = null;
        String blackUsername = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        whiteUsername = rs.getString("WhiteUsername");
                        blackUsername = rs.getString("BlackUsername");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not get player color");
        }
        if (userName.equals(whiteUsername)) {
            return "WHITE";
        } else if (userName.equals(blackUsername)) {
            return "BLACK";
        }
        return null;
    }

    public String getPlayerName(String gameID, String playerColor) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE id = \"" + gameID + "\"";
        String whiteUsername = null;
        String blackUsername = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        whiteUsername = rs.getString("WhiteUsername");
                        blackUsername = rs.getString("BlackUsername");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not get player color");
        }
        if (playerColor.equals("WHITE")) {
            return whiteUsername;
        } else if (playerColor.equals("BLACK")) {
            return blackUsername;
        }
        return null;
    }

    public ChessGame makeMove(String gameID, ChessMove move, String username) throws DataAccessException {
        ChessGame game = getGameByID(gameID);
        String playerColor = getPlayerColor(gameID, username);
        if (playerColor == null) {
            return null;
        }
        if (game.getTeamTurn().equals(ChessGame.TeamColor.WHITE) && playerColor.equals("BLACK")) {
            return null;
        }
        if (game.getTeamTurn().equals(ChessGame.TeamColor.BLACK) && playerColor.equals("WHITE")) {
            return null;
        }
        try {
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            return null;
        }
        String statement = "UPDATE games SET game = ? WHERE id = ?";
        SQLAuthDAO.executeUpdate(statement, gson.toJson(game), gameID);
        return game;
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
