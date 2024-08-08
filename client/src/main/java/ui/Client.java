package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_BG_COLOR;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private boolean signedIn = false;
    private String username;
    private String authToken;
    private WebSocketFacade ws;
    private boolean gamePlay = false;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd) {
                case "login" -> signIn(params);
                case "register" -> register(params);
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "redraw" -> redraw(params);
                case "leave" -> leaveGame(params);
                case "makeMove" -> makeMove(params);
                case "resign" -> resign(params);
                case "highlight" -> highlight(params);
                case "quit" -> quit(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String signIn(String[] params) {
        if (params.length == 2) {
            signedIn = true;
            username = params[0];
            String passUsername = params[0];
            String password = params[1];
            authToken = server.login(passUsername, password);
            if (authToken == null) {
                return """
                        Sorry, the username and password entered didn't match our database.
                        Please make sure you're typing the password correctly, and that if this is your first time
                        you use register instead of login""";
            }
            return String.format("You are logged in as %s", username);
        }
        return "Expected: <yourUserName> <yourPassword>";
    }

    public String register(String[] params) {
        if (params.length == 3) {
            signedIn = true;
            username = params[0];
            String passUsername = params[0];
            String password = params[1];
            String email = params[2];
            authToken = server.register(passUsername, password, email);
            if (authToken == null) {
                return "Sorry, that username is already taken. Please try again with a different one";
            }
            return String.format("You are registered as %s", username);
        }
        return "Expected: <yourUserName> <yourPassword> <yourEmail>";
    }

    public String logout(String[] params) {
        if (params.length > 0) {
            return "Too many arguments";
        }
        if (signedIn) {
            signedIn = false;
            server.logout(authToken);
            authToken = null;
            return "You are logged out";
        }
        return "You are not logged in, so you can not log out";
    }

    public String createGame(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (params.length == 1) {
            String gameName = params[0];
            server.createGame(authToken, gameName);
            return String.format("game %s created",gameName);
        }
        return "Expected: <gameName>";
    }

    public String listGames(String[] params) {
        if (params.length > 0) {
            return "Too many arguments";
        }
        if (!signedIn) {
            return "You are not logged in";
        }
        StringBuilder builder = new StringBuilder();
        ArrayList<GameData> games = server.listGames(authToken);
        if (games.isEmpty()) {
            return "No games found";
        }
        boolean first = true;
        int offset = 1;
        for (GameData gameData : games) {
            if (first) {
                offset = Integer.parseInt(gameData.gameID()) - 1;
                first = false;
            }
            builder.append("Game Number: ");
            builder.append(Integer.parseInt(gameData.gameID()) - offset);
            builder.append("\n");
            builder.append("Game Name: ");
            builder.append(gameData.gameName());
            builder.append("\n");
            builder.append("White: ");
            builder.append(gameData.whiteUsername());
            builder.append("\n");
            builder.append("Black: ");
            builder.append(gameData.blackUsername());
            builder.append("\n");
        }
        return builder.toString();
    }

    public String playGame(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (params.length == 2) {
            ChessGame game = new ChessGame();
            ArrayList<GameData> games = server.listGames(authToken);
            int offset = Integer.parseInt(games.getFirst().gameID()) - 1;
            int gameNumber = 1;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Sorry, you entered a word in for the gameNumber";
            }
            String passNumber = Integer.toString(gameNumber + offset);
            String playerColor = params[1].toUpperCase();
            server.joinGame(authToken, passNumber, playerColor);
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinGame(authToken, gameNumber, username);
            gamePlay = true;
            return String.format("You have joined the game as %s", playerColor);
        }
        return "Expected: <gameNumber> <playerColor>";
    }

    public String observeGame(String[] params) {
        if (params.length != 1) {
            return "incorrect number of arguments";
        }
        try {
            int gameID = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return "Sorry, you entered a word for the gameID and not a number";
        }
        ChessGame game = new ChessGame();
        printBoard(game, "BLACK");
        System.out.println();
        printBoard(game, "WHITE");
        return "";
    }

    public String redraw(String[] params) {
        return "not written yet";
    }

    public String leaveGame(String[] params) {
        return "not written yet";
    }

    public String makeMove(String[] params) {
        return "not written yet";
    }

    public String resign(String[] params) {
        gamePlay = false;
        return "not written yet";
    }

    public String highlight(String[] params) {
        return "not written yet";
    }

    public String quit(String[] params) {
        if (params.length != 0) {
            return "the quit command doesn't take any arguments";
        }
        if (gamePlay) {
            gamePlay = false;
            resign(params);
        }
        return "quit";
    }

    public String help() {
        if (!signedIn) {
            return """
                    Since you are not signed in, you can either quit, login, register, or ask for help
                    using one of the following commands
                    quit
                    login <username> <password>
                    register <username> <password> <email>
                    help
                    """;
        } if (gamePlay) {
            return """
                    You are currently playing a chess game. You can redraw the chess board, leave, make a move,
                    resign, highlight the legal moves of a given piece, or ask for help using
                    one of the following commands
                    redraw
                    leave
                    makeMove
                    resign
                    highlight
                    help
                    """;
        }
        return """
                You are currently signed in. You can logout, create a game, ask for a list
                of the current games, play a game, observe a game, or ask for help
                using one of the following commands
                logout
                create <gameName>
                Warning: after you have created a game you need to use the play command in order to join it
                list
                play <gameNumber> <playerColor>
                observe <gameNumber>
                help
                """;
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
