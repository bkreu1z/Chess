package dataaccess;

import model.GameData;

import java.util.Set;

public class GameDAO implements GameInterface {
    @Override
    public String createGame(String gameName) {
        return MEMORY_GAME_DAO.addGame(gameName);
    }

    @Override
    public boolean getGameByName(String gameName) {
        return MEMORY_GAME_DAO.getGameByName(gameName);
    }

    @Override
    public Set<GameData> getAllGames() {
        return MEMORY_GAME_DAO.listGames();
    }

    @Override
    public boolean joinGame(String username, String playerColor, String gameID) {
        return MEMORY_GAME_DAO.joinGame(username, playerColor, gameID);
    }

    @Override
    public boolean deleteGame(String gameName) {
        if (getGameByName(gameName)) {
            return MEMORY_GAME_DAO.deleteGame(gameName);
        }
        return true;
    }

    @Override
    public void clear() {
        MEMORY_GAME_DAO.clear();
    }
}
