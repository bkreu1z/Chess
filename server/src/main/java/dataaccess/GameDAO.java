package dataaccess;

import model.GameData;

import java.util.Set;

public class GameDAO implements GameInterface {
    @Override
    public String createGame(String gameName) {
        return memoryGameDAO.addGame(gameName);
    }

    @Override
    public boolean getGameByName(String gameName) {
        return memoryGameDAO.getGameByName(gameName);
    }

    @Override
    public Set<GameData> getAllGames() {
        return memoryGameDAO.listGames();
    }

    @Override
    public boolean joinGame(String username, String playerColor, String gameID) {
        return memoryGameDAO.joinGame(username, playerColor, gameID);
    }

    @Override
    public boolean deleteGame(String gameName) {
        if (getGameByName(gameName)) {
            return memoryGameDAO.deleteGame(gameName);
        }
        return true;
    }

    @Override
    public void clear() {
        memoryGameDAO.clear();
    }
}
