package service;

import requests.*;
import responses.*;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceUnitTests {
    ClearService clearService;
    GameService gameService;
    UserService userService;

    @BeforeEach
    void setUp() {
        clearService = new ClearService();
        try {
            clearService.clear();
        } catch (DataAccessException e) {
            System.out.println("Error while clearing service");
        }
        userService = new UserService();
        gameService = new GameService();
        String token = null;
        try {
            token = userService.registerUser(
                    new RegisterRequest("username", "password", "notarealemail@hotmail.com")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            gameService.createGame(new CreateRequest(token,"gameName"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            userService.logoutUser(new LogoutRequest(token));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void goodRegister() {
        userService = new UserService();
        String authToken = null;
        try {
            authToken = userService.registerUser(
                    new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com")).authToken();
            assertNotNull(authToken);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            userService.logoutUser(new LogoutRequest(authToken));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void badRegister() {
        userService = new UserService();
        RegisterResult firstResult = null;
        RegisterResult secondResult = null;
        try {
            firstResult = userService.registerUser(
                    new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
             secondResult = userService.registerUser(
                     new RegisterRequest("2kewl", "evencoolier", "stillnotarealemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(firstResult);
        assertNull(secondResult);
    }

    @Test
    void goodLogin() {
        userService = new UserService();
        String token = null;
        try {
            token = userService.loginUser(new LoginRequest("username", "password")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(token);
    }

    @Test
    void badLogin() {
        userService = new UserService();
        LoginResult loginResult;
        goodRegister();
        try {
            loginResult = userService.loginUser(new LoginRequest("username", "notthecooliest"));
        } catch (DataAccessException e) {
            loginResult = new LoginResult("wrong password", "wrong password");
        }
        assertEquals("wrong password", loginResult.username());
        assertEquals("wrong password", loginResult.authToken());
    }

    public boolean logOutHelper(String switcher) {
        userService = new UserService();
        String token = "";
        boolean checker = true;
        try {
            token =
                    userService.registerUser(
                            new RegisterRequest("boringusername", "boringpassword", "boringemail@hotmail.com")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        if (switcher.equals("good")) {
            try {
                userService.logoutUser(new LogoutRequest(token));
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
                checker = false;
            }
        }
        if (switcher.equals("bad")) {
            try {
                userService.logoutUser(new LogoutRequest("randomstring"));
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
                logOutHelper("good");
                checker = false;
            }
        }
        return checker;
    }

    @Test
    void goodLogout() {
        assertTrue(logOutHelper("good"));
    }

    @Test
    void badLogout() {
        assertFalse(logOutHelper("bad"));
    }

    @Test
    void goodCreateGame() {
        goodRegister();
        userService = new UserService();
        gameService = new GameService();
        String authToken = null;
        String gameID = "";
        try {
            authToken = userService.loginUser(new LoginRequest("username", "password")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            gameID = gameService.createGame(new CreateRequest(authToken,"Name")).gameID();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        String expected = "2";
        assertEquals(expected, gameID);
    }

    @Test
    void badCreateGame() {
        userService = new UserService();
        gameService = new GameService();
        CreateResult createResult = null;
        try {
            createResult = gameService.createGame(new CreateRequest("badtoken","Name"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNull(createResult);
    }

    @Test
    void goodListGames() {
        userService = new UserService();
        gameService = new GameService();
        String authToken = "";
        ListResult listResult = null;
        try {
            authToken = userService.registerUser(
                    new RegisterRequest("mountain", "climber", "room@hotmail.com")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            listResult = gameService.listGames(new ListRequest(authToken));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(listResult);
    }

    @Test
    void badListGames() {
        userService = new UserService();
        gameService = new GameService();
        boolean checker = false;
        try {
            gameService.listGames(new ListRequest("badtoken"));
        } catch (DataAccessException e) {
            checker = true;
        }
        assertTrue(checker);
    }

    @Test
    void goodJoin() {
        userService = new UserService();
        gameService = new GameService();
        String authToken = null;
        String gameID = null;
        JoinResult joinResult = null;
        try {
            authToken = userService.registerUser(
                    new RegisterRequest("goodJoin", "goodJoin", "goodJoin@gmail.com")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try{
            gameID = gameService.createGame(new CreateRequest(authToken, "George")).gameID();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            joinResult = gameService.joinGame(new JoinRequest(authToken, "BLACK", gameID));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(joinResult);
    }

    @Test
    void badJoin() {
        userService = new UserService();
        gameService = new GameService();
        String authToken = "badToken";
        String gameID = "3";
        JoinResult joinResult = null;
        try {
            joinResult = gameService.joinGame(new JoinRequest(authToken, "BLACK", gameID));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNull(joinResult);
    }

    @Test
    void clear() {
        userService = new UserService();
        String token = " ";
        try{
            token = userService.registerUser(
                    new RegisterRequest("anybody", "outthere", "room@hotmail.com")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        gameService = new GameService();
        try{
            gameService.createGame(new CreateRequest(token, "Steve"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        ClearResult result = null;
        clearService = new ClearService();
        try {
            result = clearService.clear();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        ClearResult expectedClearResult = new ClearResult();
        assertEquals(expectedClearResult, result);
    }
}
