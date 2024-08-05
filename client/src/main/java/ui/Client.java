package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_BG_COLOR;

public class Client {
    private final ServerFacade server;
    private final Repl repl;
    private final String serverUrl;
    private boolean signedIn = false;
    private String username;
    private String authToken;

    public Client(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd) {
                case "login" -> signIn(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String signIn(String[] params) {
        if (params.length >= 2) {
            signedIn = true;
            username = params[0];
            String passUsername = params[0];
            String password = params[1];
            authToken = server.login(passUsername, password);
            return String.format("You are logged in as %s", username);
        }
        return "Expected: <yourUserName> <yourPassword>";
    }

    public String register(String[] params) {
        if (params.length >= 3) {
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

    public String logout() {
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
        if (params.length >= 1) {
            String gameName = params[0];
            server.createGame(authToken, gameName);
        }
        return "Expected: <gameName>";
    }

    public String listGames() {
        if (!signedIn) {
            return "You are not logged in";
        }
        return server.listGames(authToken);
    }

    public String playGame(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (params.length >= 2) {
            String gameNumber = params[0];
            String playerColor = params[1];
            server.joinGame(authToken, gameNumber, username, playerColor);
            return String.format("You have joined the game as %s", playerColor);
        }
        return "Expected: <gameNumber> <playerColor>";
    }

    public String observeGame(String[] params) {
        ChessGame game = new ChessGame();
        printBoard(game, "BLACK");
        System.out.println();
        printBoard(game, "WHITE");
        return "";
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
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
            for (String row : boardString) {
                System.out.print(row);
            }
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
            System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR);
        }
        if (bottomColor.equals("WHITE")) {
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
            int currentRow = boardString.size() - 1;
            while (currentRow >= 0) {
                System.out.print(boardString.get(currentRow));
                currentRow--;
            }
            System.out.println(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + SET_TEXT_BOLD + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
            System.out.println(RESET_BG_COLOR + RESET_TEXT_COLOR);
        }
    }

    public static ArrayList<String> makeRows(ChessBoard board, String bottomColor) {
        ArrayList<String> rowArray = new ArrayList<>();
        int rowNum = 1;
        if (bottomColor.equals("WHITE")) {
            rowNum = 8;
        }
        boolean isLight = true;
        String onSquare = "   ";
        for (ChessPosition[] row : board.getSpaces()) {
            StringBuilder builder = new StringBuilder();
            builder.append(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + " " + rowNum + " ");
            for (ChessPosition square : row) {
                ChessPiece piece = square.getPiece();
                String pieceColorName = "";
                if (piece != null) {
                    ChessPiece.PieceType pieceName = piece.getPieceType();
                    ChessGame.TeamColor pieceColor = piece.getTeamColor();
                    switch(pieceName) {
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
                }
                else {
                    onSquare = "   ";
                }
                if (isLight) {
                    builder.append(SET_BG_COLOR_LIGHT_GREY + pieceColorName + onSquare);
                    isLight = false;
                }
                else {
                    builder.append(SET_BG_COLOR_DARK_GREY + pieceColorName + onSquare);
                    isLight = true;
                }
            }
            builder.append(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + " " + rowNum + " " + RESET_BG_COLOR + "\n");
            if (isLight) {
                isLight = false;
            }
            else {
                isLight = true;
            }
            if (bottomColor.equals("WHITE")) {
                rowNum--;
            }
            else {
                rowNum++;
            }
            String finishedRow = builder.toString();
            rowArray.add(finishedRow);
        }
        return rowArray;
    }
}
