package ui;

import chess.*;
import model.GameData;
import server.ServerFacade;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.*;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private boolean signedIn = false;
    private String username;
    private String authToken;
    private WebSocketFacade ws;
    private boolean gamePlay = false;
    private Integer gameID = null;
    private String playerColor = "WHITE";

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        ws = new WebSocketFacade(serverUrl, notificationHandler);
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
                case "makemove" -> makeMove(params);
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
            ArrayList<GameData> games = server.listGames(authToken);
            int offset = Integer.parseInt(games.getFirst().gameID()) - 1;
            int gameNumber = 1;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Sorry, you entered a word in for the gameNumber";
            }
            String passNumber = Integer.toString(gameNumber + offset);
            playerColor = params[1].toUpperCase();
            notificationHandler.setPlayerColor(playerColor);
            gameID = gameNumber + offset;
            server.joinGame(authToken, passNumber, playerColor);
            ws.joinGame(authToken, gameID);
            gamePlay = true;
            return String.format("You have joined the game as %s", playerColor);
        }
        return "Expected: <gameNumber> <playerColor>";
    }

    public String observeGame(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (params.length != 1) {
            return "incorrect number of arguments";
        }
        ArrayList<GameData> games = server.listGames(authToken);
        Integer offset = Integer.parseInt(games.getFirst().gameID()) - 1;
        try {
            gameID = Integer.parseInt(params[0]) + offset;
        } catch (NumberFormatException e) {
            return "Sorry, you entered a word for the gameID and not a number";
        }
        ws.joinGame(authToken, gameID);
        gamePlay = true;
        return "";
    }

    public String redraw(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (params.length != 0) {
            return "incorrect number of arguments";
        }
        if (gameID == null || !gamePlay) {
            return "you are not currently in a game and cannot redraw the board";
        }
        notificationHandler.redrawBoard();
        return "";
    }

    public String leaveGame(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (params.length != 0) {
            return "incorrect number of arguments";
        }
        if (gameID == null || !gamePlay) {
            return "you are not currently in a game and cannot leave";
        }
        ws.leaveGame(authToken, gameID);
        gamePlay = false;
        gameID = null;
        return "You have left the game";
    }

    public String makeMove(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (gameID == null || !gamePlay) {
            return "you are not currently in a game and cannot make a move";
        }
        if (params.length != 4 && params.length != 5) {
            return "incorrect number of arguments";
        }
        int startX;
        int startY;
        int endX;
        int endY;
        String promotion = "";
        try {
            startX = Integer.parseInt(params[0]);
            startY = Integer.parseInt(params[1]);
            endX = Integer.parseInt(params[2]);
            endY = Integer.parseInt(params[3]);
            if (params.length == 5) {
                promotion = params[4].toUpperCase();
            }
        } catch (NumberFormatException e) {
            return "Sorry, you entered a word for something that's supposed to be a number";
        }
        ChessPosition startPosition = new ChessPosition(startY, startX);
        ChessPosition endPosition = new ChessPosition(endY, endX);
        ChessPiece.PieceType pieceType = null;
        switch (promotion) {
            case "ROOK" -> pieceType = ChessPiece.PieceType.ROOK;
            case "KNIGHT" -> pieceType = ChessPiece.PieceType.KNIGHT;
            case "BISHOP" -> pieceType = ChessPiece.PieceType.BISHOP;
            case "KING" -> pieceType = ChessPiece.PieceType.KING;
            default -> pieceType = null;
        }
        ChessMove move = new ChessMove(startPosition, endPosition, pieceType);
        ws.makeMove(authToken, gameID, move);
        return "";
    }

    public String resign(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (params.length != 0) {
            return "incorrect number of arguments";
        }
        if (gameID == null || !gamePlay) {
            return "you are not currently in a game and cannot leave";
        }
        System.out.println("Are you sure you would like to resign? Yes/No");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (line.equalsIgnoreCase("yes")) {
            ws.resign(authToken, gameID);
            gamePlay = false;
            gameID = null;
            notificationHandler.setPlayerColor(null);
        }
        return "";
    }

    public String highlight(String[] params) {
        if (!signedIn) {
            return "You are not logged in";
        }
        if (gameID == null || !gamePlay) {
            return "you are not currently in a game and cannot highlight moves";
        }
        if (params.length != 2) {
            return "incorrect number of arguments";
        }
        ChessPosition startPosition = null;
        try {
            startPosition = new ChessPosition(Integer.parseInt(params[1]), Integer.parseInt(params[0]));
        } catch (NumberFormatException e) {
            return "sorry, you passed in a number instead of a word";
        }
        ChessGame game = notificationHandler.getGame();
        Collection<ChessMove> validMoves = game.validMoves(startPosition);
        Collection<ChessPosition> validEnds = new HashSet<>();
        for (ChessMove move : validMoves) {
            validEnds.add(move.getEndPosition());
        }
        notificationHandler.printHighlight(validEnds);
        return "";
    }

    public String quit(String[] params) {
        if (params.length != 0) {
            return "the quit command doesn't take any arguments";
        }
        if (gamePlay) {
            gamePlay = false;
            resign(params);
        }
        if (signedIn) {
            logout(params);
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
                    makeMove <startX> <startY> <endX> <endY> <promotion(optional)>
                    resign
                    highlight <startX> <startY>
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

}
