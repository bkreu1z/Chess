package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;

public class Repl implements NotificationHandler {
    public String playerColor;

    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to Chess! Log in to start");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print("\n>>>");
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (!result.equals("quit")) {
                    System.out.print(result);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage errorMessage = (ErrorMessage) serverMessage;
            System.out.println(errorMessage.getErrorMessage());
            System.out.println("ErrorMessage");
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
            System.out.println(notificationMessage.getNotificationMessage());
            System.out.println("NotificationMessage");
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
            ChessGame gameBoard = loadGameMessage.getGame();
            if (playerColor.equals("BLACK")) {
                printBoard(gameBoard, "BLACK");
            } else {
                printBoard(gameBoard, "WHITE");
            }
        }
        System.out.print(">>>");
    }

    @Override
    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public static void printBoard(ChessGame game, String bottomColor) {
        ArrayList<String> boardString = makeRows(game.getBoard(), bottomColor);
        if (bottomColor.equals("BLACK")) {
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD +
                    "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
            for (String row : boardString) {
                System.out.print(row);
            }
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD +
                    "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
            System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR);
        }
        if (bottomColor.equals("WHITE")) {
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD +
                    "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
            int currentRow = boardString.size() - 1;
            while (currentRow >= 0) {
                System.out.print(boardString.get(currentRow));
                currentRow--;
            }
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD +
                    "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
            System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR);
        }
    }

    public static ArrayList<String> makeRows(ChessBoard board, String bottomColor) {
        ArrayList<String> rowArray = new ArrayList<>();
        int rowNum = 1;
        boolean isLight = true;
        if (bottomColor.equals("WHITE")) {
            isLight = false;
        }
        for (ChessPosition[] row : board.getSpaces()) {
            StringBuilder builder = new StringBuilder();
            builder.append(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + " " + rowNum + " ");
            if (bottomColor.equals("BLACK")) {
                ChessPosition[] reverseRow = new ChessPosition[row.length];
                for (int i = 0; i <= 7; i++) {
                    reverseRow[i] = row[7 - i];
                }
                buildRow(reverseRow, builder, isLight);
            }
            else {
                buildRow(row, builder, isLight);
            }
            builder.append(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + " " + rowNum + " " + RESET_BG_COLOR + "\n");
            if (isLight) {
                isLight = false;
            }
            else {
                isLight = true;
            }
            rowNum++;
            String finishedRow = builder.toString();
            rowArray.add(finishedRow);
        }
        return rowArray;
    }

    public static void buildRow(ChessPosition[] row, StringBuilder builder, boolean isLight) {
        String onSquare = "   ";
        for (ChessPosition square : row) {
            ChessPiece piece = square.getPiece();
            String pieceColorName = "";
            if (piece != null) {
                ChessPiece.PieceType pieceName = piece.getPieceType();
                ChessGame.TeamColor pieceColor = piece.getTeamColor();
                switch (pieceName) {
                    case KING -> onSquare = " K ";
                    case QUEEN -> onSquare = " Q ";
                    case BISHOP -> onSquare = " B ";
                    case KNIGHT -> onSquare = " N ";
                    case ROOK -> onSquare = " R ";
                    case PAWN -> onSquare = " P ";
                }
                switch (pieceColor) {
                    case WHITE -> pieceColorName = SET_TEXT_COLOR_WHITE;
                    case BLACK -> pieceColorName = SET_TEXT_COLOR_RED;
                }
            } else {
                onSquare = "   ";
            }
            if (isLight) {
                builder.append(SET_BG_COLOR_LIGHT_GREY + pieceColorName + onSquare);
                isLight = false;
            } else {
                builder.append(SET_BG_COLOR_DARK_GREY + pieceColorName + onSquare);
                isLight = true;
            }
        }
    }
}
