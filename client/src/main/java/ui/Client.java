package ui;

import server.ServerFacade;

import java.util.Arrays;

public class Client {
    private final ServerFacade server;
    private final Repl repl;
    private final String serverUrl;
    private boolean signedIn = false;
    private String username;

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
            server.login(passUsername, password);
            return String.format("You are logged in as %s", username);
        }
        return "Expected: <yourUserName> <yourPassword>";
    }

    public String register(String[] params) {
        return "";
    }

    public String logout() {
        return "";
    }

    public String createGame(String[] params) {
        return "";
    }

    public String listGames() {
        return "";
    }

    public String playGame(String[] params) {
        return "";
    }

    public String observeGame(String[] params) {
        return "";
    }

    public String help() {
        if (!signedIn) {
            return """
                    Since you are not signed in, you can either quit, login, or register,
                    using one of the following commands
                    -quit
                    -login <username> <password>
                    -register <username> <password> <email>
                    """;
        }
        return """
                You are currently signed in. You can logout, create a game, ask for a list
                of the current games, play a game, or observe a game, using one of the following commands
                -logout
                -create <gameName>
                after you have created a game you need to use the play command in order to join it
                -list
                -play <gameNumber> <playerColor>
                -observe <gameNumber>
                """;
    }
}
