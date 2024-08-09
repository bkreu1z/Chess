package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String userName, Session session, Integer gameID) {
        var connection = new Connection(userName, session, gameID);
        connections.put(userName, connection);
    }

    public void remove(String userName) {
        connections.remove(userName);
    }

    public void broadcast(String excludeUserName, ServerMessage serverMessage, Integer gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.userName.equals(excludeUserName) && c.gameID.equals(gameID)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.userName);
        }
    }

    public void singleUserBroadcast(String user, ServerMessage serverMessage) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.userName.equals(user)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            }
        }
    }
}
