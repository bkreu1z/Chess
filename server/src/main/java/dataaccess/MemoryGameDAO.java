package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Set;

public class MemoryGameDAO {
    Set<GameData> games;
    int currentID = 1;

    public MemoryGameDAO() {}

    public int addGame(String gameName) {
        games.add(new GameData(currentID, null, null, gameName, new ChessGame()));
        int gameID = currentID;
        currentID++;
        return gameID;
    }

    public boolean getGameByName(String gameName) {
        for (GameData gameData : games) {
            if (gameData.gameName().equals(gameName)) {
                return true;
            }
        }
        return false;
    }

    public Set<GameData> listGames() {
        return games;
    }

    public boolean joinGame(String username, String playerColor, int gameID) {
        for (GameData gameData : games) {
            if (gameData.gameID() == gameID) {
                String whiteName = gameData.whiteUsername();
                String blackName = gameData.blackUsername();
                String gameName = gameData.gameName();
                ChessGame chessGame = gameData.game();
                if (playerColor.equalsIgnoreCase("BLACK") && gameData.blackUsername() == null) {
                    games.remove(gameData);
                    games.add(new GameData(gameID, whiteName, username, gameName, chessGame));
                    return true;
                }
                else if (playerColor.equalsIgnoreCase("WHITE") && gameData.whiteUsername() == null) {
                    games.remove(gameData);
                    games.add(new GameData(gameID, username, blackName, gameName, chessGame));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean deleteGame(String gameName) {
        for (GameData gameData : games) {
            if (gameData.gameName().equals(gameName)) {
                games.remove(gameData);
                return true;
            }
        }
        return false;
    }

    public void clear() {
        games.clear();
    }
}
