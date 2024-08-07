package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(action.getUserName(), session);
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave(action.getUserName());
            case RESIGN -> resign();
        }
    }

    private void connect(String userName, Session session) throws IOException {
        connections.add(userName, session);
        var message = String.format("%s has joined the game", userName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(userName, notification);
    }

    private void makeMove() {}

    private void leave(String userName) throws IOException {
        connections.remove(userName);
        var message = String.format("%s has left the game", userName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(userName, notification);
    }

    private void resign() {}
}
