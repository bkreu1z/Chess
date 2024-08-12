package ui;

import chess.*;
import ui.websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;

public class Repl implements NotificationHandler {
    public String playerColor = "";
    public ChessGame game;

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
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
            System.out.println(notificationMessage.getNotificationMessage());
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
            ChessGame gameBoard = loadGameMessage.getGame();
            this.game = gameBoard;
            if (playerColor.equals("BLACK")) {
                printBoard(gameBoard, "BLACK", null);
            } else {
                printBoard(gameBoard, "WHITE", null);
            }
        }
        System.out.print(">>>");
    }

    @Override
    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public void redrawBoard() {
        if (playerColor.equals("BLACK")) {
            printBoard(game, "BLACK", null);
        } else {
            printBoard(game, "WHITE", null);
        }
    }

    @Override
    public ChessGame getGame() {
        return game;
    }

    @Override
    public void printHighlight(Collection<ChessPosition> validEnds) {
        if (playerColor.equals("BLACK")) {
            printBoard(game, "BLACK", validEnds);
        } else {
            printBoard(game, "WHITE", validEnds);
        }
    }

    public static void printBoard(ChessGame game, String bottomColor, Collection<ChessPosition> validMoves) {
        ArrayList<String> boardString = makeRows(game.getBoard(), bottomColor, validMoves);
        System.out.println("\n");
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

    public static ArrayList<String> makeRows(ChessBoard board, String bottomColor, Collection<ChessPosition> validMoves) {
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
                buildRow(reverseRow, builder, isLight, validMoves);
            }
            else {
                buildRow(row, builder, isLight, validMoves);
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

    public static void buildRow(ChessPosition[] row, StringBuilder builder, boolean isLight, Collection<ChessPosition> validMoves) {
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
            builder.append(makeSquare(square, validMoves, isLight, pieceColorName, onSquare));
            if (isLight) {
                isLight = false;
            } else {
                isLight = true;
            }
        }
    }

    public static String makeSquare(ChessPosition square, Collection<ChessPosition> validMoves,
                                    boolean isLight, String pieceColorName, String onSquare) {
        if (validMoves == null) {
            if (isLight) {
                return SET_BG_COLOR_LIGHT_GREY + pieceColorName + onSquare;
            }
            else {
                return SET_BG_COLOR_DARK_GREY + pieceColorName + onSquare;
            }
        }
        if (isLight) {
            for (ChessPosition end : validMoves) {
                if (end.equals(square)) {
                    return SET_BG_COLOR_GREEN + pieceColorName + onSquare;
                }
            }
            return SET_BG_COLOR_LIGHT_GREY + pieceColorName + onSquare;
        }
        else {
            for (ChessPosition end : validMoves) {
                if (end.equals(square)) {
                    return SET_BG_COLOR_DARK_GREEN + pieceColorName + onSquare;
                }
            }
            return SET_BG_COLOR_DARK_GREY + pieceColorName + onSquare;
        }
    }
}
