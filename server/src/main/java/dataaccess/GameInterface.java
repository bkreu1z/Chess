package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameInterface {
    MemoryGameDAO MEMORY_GAME_DAO = new MemoryGameDAO();

    String createGame(String gameName) throws DataAccessException;

    boolean getGameByName(String gameName) throws DataAccessException;

    ArrayList<GameData> getAllGames() throws DataAccessException;

    boolean joinGame(String username, String playerColor, String gameID);

    boolean deleteGame(String gameName);

    void clear();
}
