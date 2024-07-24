package service;

import requests.CreateRequest;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.ClearResult;
import responses.CreateResult;
import responses.LoginResult;
import responses.RegisterResult;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceUnitTests {
    ClearService clearService;
    GameService gameService;
    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        gameService = new GameService();
        RegisterResult registerResult = null;
        try {
            registerResult = userService.registerUser(new RegisterRequest("username", "password", "notarealemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            gameService.createGame(new CreateRequest(registerResult.authToken(),"gameName"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            userService.logoutUser(new LogoutRequest(registerResult.authToken()));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void goodRegister() {
        userService = new UserService();
        RegisterResult result = null;
        try {
            result = userService.registerUser(new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com"));
            assertNotNull(result.authToken());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            userService.logoutUser(new LogoutRequest(result.authToken()));
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
            firstResult = userService.registerUser(new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com"));
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
        LoginResult loginResult = null;
        try {
            loginResult = userService.loginUser(new LoginRequest("username", "password"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(loginResult.authToken());
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
        RegisterResult registerResult = null;
        boolean checker = true;
        try {
            registerResult =
                    userService.registerUser(new RegisterRequest("boringusername", "boringpassword", "boringemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        if (switcher.equals("good")) {
            try {
                userService.logoutUser(new LogoutRequest(registerResult.authToken()));
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
        CreateResult createResult = null;
        try {
            authToken = userService.loginUser(new LoginRequest("username", "password")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            createResult = gameService.createGame(new CreateRequest(authToken,"Name"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        String expected = "2";
        assertEquals(expected, createResult.gameID());
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
    void clear() {
        goodRegister();
        goodCreateGame();
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
