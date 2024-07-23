package dataaccess;

import model.GameData;

import java.util.Set;

public interface GameInterface {
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    int createGame(String gameName) throws Exception;

    boolean getGameByName(String gameName);

    Set<GameData> getAllGames();

    boolean joinGame(String username, String playerColor, int gameID);

    boolean deleteGame(String gameName);

    void clear();
}
