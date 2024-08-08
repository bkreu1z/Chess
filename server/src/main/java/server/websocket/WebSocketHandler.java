package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private SQLGameDAO gameDAO = new SQLGameDAO();
    private SQLAuthDAO authDAO = new SQLAuthDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(action.getAuthString(), session, action.getGameID());
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave(action.getAuthString());
            case RESIGN -> resign();
        }
    }

    private void connect(String authString, Session session, Integer gameID) throws IOException, DataAccessException {
        String username = authDAO.getUsername(authString);
        connections.add(username, session);
        var message = String.format("%s has joined the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(username, notification);
        var game = gameDAO.getGameByID(Integer.toString(gameID));
        var soloNotification = new LoadGameMessage(game);
        connections.singleUserBroadcast(username, soloNotification);
    }

    private void makeMove() {}

    private void leave(String authToken) throws IOException {
        connections.remove(authToken);
        var message = String.format("%s has left the game", authToken);
        var notification = new NotificationMessage(message);
        connections.broadcast(authToken, notification);
    }

    private void resign() {}
}
