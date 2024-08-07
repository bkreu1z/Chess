package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
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
            case LEAVE -> leave(action.getAuthString(), action.getGameID());
            case RESIGN -> resign();
        }
    }

    private void connect(String authString, Session session, Integer gameID) throws IOException, DataAccessException {
        String username = authDAO.getUsername(authString);
        if (!username.equals("")) {
            connections.add(username, session);
            var game = gameDAO.getGameByID(Integer.toString(gameID));
            if (game != null) {
                var message = String.format("%s has joined the game", username);
                var notification = new NotificationMessage(message);
                connections.broadcast(username, notification);
                var soloNotification = new LoadGameMessage(game);
                connections.singleUserBroadcast(username, soloNotification);
            } else {
                connections.singleUserBroadcast(username, new ErrorMessage("invalid game ID"));
            }
        } else {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("invalid authentication. You may not be logged in")));
        }
    }

    private void makeMove() {}

    private void leave(String authToken, Integer gameID) throws IOException, DataAccessException {
        String username = authDAO.getUsername(authToken);
        String playerColor = gameDAO.getPlayerColor(Integer.toString(gameID), username);
        if (playerColor != null) {
            gameDAO.leaveGame(Integer.toString(gameID), playerColor);
        }
        connections.remove(username);
        var message = String.format("%s has left the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(username, notification);
    }

    private void resign() {}
}
