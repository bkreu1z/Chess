package ui.websocket;


import chess.ChessGame;
import chess.ChessPosition;
import websocket.messages.ServerMessage;

import java.util.Collection;

public interface NotificationHandler {
    public String PLAYER_COLOR = null;
    public ChessGame GAME = null;

    void notify(ServerMessage serverMessage);

    void setPlayerColor(String playercolor);

    void redrawBoard();

    ChessGame getGame();

    void printHighlight(Collection<ChessPosition> validEnds);
}
