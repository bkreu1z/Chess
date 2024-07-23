package Handlers;

import com.google.gson.Gson;
import service.ClearService;
import service.GameService;
import service.UserService;

public interface HandlerInterface {
    Gson gson = new Gson();
    UserService userService = new UserService();
    GameService gameService = new GameService();
    ClearService clearService = new ClearService();
}
