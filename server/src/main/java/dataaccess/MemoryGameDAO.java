package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MemoryGameDAO {
    ArrayList<GameData> games;
    int currentID = 1;

    public MemoryGameDAO() {
        games = new ArrayList<>();
    }

    public String addGame(String gameName) {
        String gameID = Integer.toString(currentID);
        games.add(new GameData(gameID, null, null, gameName, new ChessGame()));
        currentID++;
        return gameID;
    }

    public boolean getGameByName(String gameName) {
        if (games == null) {
            return false;
        }
        for (GameData gameData : games) {
            if (gameData.gameName().equals(gameName)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<GameData> listGames() {
        return games;
    }

    public boolean joinGame(String username, String playerColor, String gameID) {
        if (games == null) {
            return false;
        }
        for (GameData gameData : games) {
            if (gameData.gameID().equals(gameID)) {
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
        if (games != null) {
            games.clear();
        }
    }
}
