package dataaccess;

import chess.ChessGame;
import model.GameData;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class SQLGameDAO implements GameInterface {
    Gson GSON = new Gson();

    @Override
    public String createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        String gameJson = GSON.toJson(game);
        String gameID = "";
        var createStatement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        try {
            SQLAuthDAO.executeUpdate(createStatement, gameName, gameJson);
        } catch (Exception e) {
            throw new DataAccessException("could not create game");
        }
        var idStatement = "SELECT id FROM games WHERE gameName = '" + gameName + "'";
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
    public boolean getGameByName(String gameName) {
        return false;
    }

    @Override
    public Set<GameData> getAllGames() {
        return Set.of();
    }

    @Override
    public boolean joinGame(String username, String playerColor, String gameID) {
        return false;
    }

    @Override
    public boolean deleteGame(String gameName) {
        return false;
    }

    @Override
    public void clear() {

    }
}
