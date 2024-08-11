package websocket.messages;

public class NotificationMessage extends ServerMessage {

    public NotificationMessage(String message) {
        this.message = message;
        this.serverMessageType = ServerMessageType.NOTIFICATION;
    }

    public String getNotificationMessage() {
        return message;
    }
}
