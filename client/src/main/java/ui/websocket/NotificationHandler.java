package ui.websocket;


import websocket.messages.ServerMessage;

public interface NotificationHandler {
    public String playercolor = null;

    void notify(ServerMessage serverMessage);

    void setPlayerColor(String playercolor);
}
