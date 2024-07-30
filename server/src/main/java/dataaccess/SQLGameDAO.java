package dataaccess;

import model.GameData;

import java.util.Set;

public class SQLGameDAO implements GameInterface {
    @Override
    public String createGame(String gameName) {
        return "";
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
