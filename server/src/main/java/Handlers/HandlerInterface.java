package handlers;

import com.google.gson.Gson;
import service.ClearService;
import service.GameService;
import service.UserService;

public interface HandlerInterface {
    Gson GSON = new Gson();
    UserService USER_SERVICE = new UserService();
    GameService GAME_SERVICE = new GameService();
    ClearService CLEAR_SERVICE = new ClearService();
}
