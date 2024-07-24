package dataaccess;

import model.GameData;

import java.util.Set;

public interface GameInterface {
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    String createGame(String gameName);

    boolean getGameByName(String gameName);

    Set<GameData> getAllGames();

    boolean joinGame(String username, String playerColor, String gameID);

    boolean deleteGame(String gameName);

    void clear();
}
