package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String userName;
    public Session session;
    public Integer gameID;

    public Connection(String userName, Session session, Integer gameID) {
        this.userName = userName;
        this.session = session;
        this.gameID = gameID;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
