package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private SQLGameDAO gameDAO = new SQLGameDAO();
    private SQLAuthDAO authDAO = new SQLAuthDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        MakeMoveCommand makeMove = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(action.getAuthString(), session, action.getGameID());
            case MAKE_MOVE -> makeMove(makeMove.getAuthString(), makeMove.getGameID(), makeMove.getMove(), session);
            case LEAVE -> leave(action.getAuthString(), action.getGameID());
            case RESIGN -> resign(action.getAuthString(), action.getGameID(), session);
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
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage("invalid authentication. You may not be logged in")));
        }
    }

    private void makeMove(String authString, Integer gameID, ChessMove move, Session session) throws IOException, DataAccessException {
        String username = authDAO.getUsername(authString);
        if (!username.equals("")) {
            if (!gameDAO.checkColorNull("WHITE", Integer.toString(gameID)) && !gameDAO.checkColorNull("BLACK", Integer.toString(gameID))) {
                ChessGame game = gameDAO.makeMove(Integer.toString(gameID), move, username);
                if (game != null) {
                    String startPosition = move.getStartPosition().toString();
                    String endPosition = move.getEndPosition().toString();
                    String message = String.format("%s moved from %s to %s", username, startPosition, endPosition);
                    var boardNotification = new LoadGameMessage(game);
                    var textNotification = new NotificationMessage(message);
                    connections.broadcast(username, boardNotification);
                    connections.broadcast(username, textNotification);
                    connections.singleUserBroadcast(username, boardNotification);

                    boolean checkmate = false;
                    if (gameDAO.getPlayerColor(Integer.toString(gameID), username).equals("WHITE")) {
                        checkmate = game.isInCheckmate(ChessGame.TeamColor.BLACK);
                    }
                    else if (gameDAO.getPlayerColor(Integer.toString(gameID), username).equals("BLACK")) {
                        checkmate = game.isInCheckmate(ChessGame.TeamColor.WHITE);
                    }
                    if (checkmate) {
                        String winMessage = String.format("%s has won the game", username);
                        String winnerMessage = "Congratulations, you have won the game!";
                        var winNotification = new NotificationMessage(winMessage);
                        var winnerNotification = new NotificationMessage(winnerMessage);
                        connections.broadcast(username, winNotification);
                        connections.singleUserBroadcast(username, winnerNotification);
                    }
                } else {
                    connections.singleUserBroadcast(username, new ErrorMessage("Invalid move. This could mean it's not " +
                            "your turn, or that the move would put you in check"));
                }
            } else {
                connections.singleUserBroadcast(username, new ErrorMessage("Not enough players to make move"));
            }
        } else {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage("invalid authentication. You may not be logged in")));
        }

    }

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

    private void resign(String authToken, Integer gameID, Session session) throws IOException, DataAccessException {
        String username = authDAO.getUsername(authToken);
        String playerColor = gameDAO.getPlayerColor(Integer.toString(gameID), username);
        if (playerColor != null) {
            String otherPlayerColor = null;
            if (playerColor.equals("WHITE")) {
                otherPlayerColor = "BLACK";
            } else if (playerColor.equals("BLACK")) {
                otherPlayerColor = "WHITE";
            }
            if (!gameDAO.checkColorNull(otherPlayerColor, Integer.toString(gameID))) {
                var userNotification = new NotificationMessage("you have resigned the game");
                connections.singleUserBroadcast(username, userNotification);
                connections.remove(username);
                gameDAO.leaveGame(Integer.toString(gameID), playerColor);
                var message = String.format("%s has resigned", username);
                var notification = new NotificationMessage(message);
                connections.broadcast(username, notification);
            } else {
                connections.singleUserBroadcast(username, new ErrorMessage("The other player is not there, so you can't" +
                        "resign. If you would like to leave please use the leave command"));
            }
        } else {
            session.getRemote().sendString(new Gson().toJson(
                    new ErrorMessage("You are observing this game and cannot resign. If you would like to leave use" +
                            "the leave command")));
        }
    }
}
