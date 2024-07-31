package dataaccess;

import model.GameData;

import java.util.Set;

public interface GameInterface {
    MemoryGameDAO MEMORY_GAME_DAO = new MemoryGameDAO();

    String createGame(String gameName) throws DataAccessException;

    boolean getGameByName(String gameName);

    Set<GameData> getAllGames();

    boolean joinGame(String username, String playerColor, String gameID);

    boolean deleteGame(String gameName);

    void clear();
}
