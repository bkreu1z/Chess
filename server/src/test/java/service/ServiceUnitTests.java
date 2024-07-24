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
    ClearService CLEAR_SERVICE;
    GameService GAME_SERVICE;
    UserService USER_SERVICE;

    @BeforeEach
    void setUp() {
        USER_SERVICE = new UserService();
        GAME_SERVICE = new GameService();
        RegisterResult registerResult = null;
        try {
            registerResult = USER_SERVICE.registerUser(new RegisterRequest("username", "password", "notarealemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            GAME_SERVICE.createGame(new CreateRequest(registerResult.authToken(),"gameName"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            USER_SERVICE.logoutUser(new LogoutRequest(registerResult.authToken()));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void goodRegister() {
        USER_SERVICE = new UserService();
        RegisterResult result = null;
        try {
            result = USER_SERVICE.registerUser(new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com"));
            assertNotNull(result.authToken());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            USER_SERVICE.logoutUser(new LogoutRequest(result.authToken()));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void badRegister() {
        USER_SERVICE = new UserService();
        RegisterResult firstResult = null;
        RegisterResult secondResult = null;
        try {
            firstResult = USER_SERVICE.registerUser(new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
             secondResult = USER_SERVICE.registerUser(
                     new RegisterRequest("2kewl", "evencoolier", "stillnotarealemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(firstResult);
        assertNull(secondResult);
    }

    @Test
    void goodLogin() {
        USER_SERVICE = new UserService();
        LoginResult loginResult = null;
        try {
            loginResult = USER_SERVICE.loginUser(new LoginRequest("username", "password"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertNotNull(loginResult.authToken());
    }

    @Test
    void badLogin() {
        USER_SERVICE = new UserService();
        LoginResult loginResult;
        goodRegister();
        try {
            loginResult = USER_SERVICE.loginUser(new LoginRequest("username", "notthecooliest"));
        } catch (DataAccessException e) {
            loginResult = new LoginResult("wrong password", "wrong password");
        }
        assertEquals("wrong password", loginResult.username());
        assertEquals("wrong password", loginResult.authToken());
    }

    public boolean logOutHelper(String switcher) {
        USER_SERVICE = new UserService();
        RegisterResult registerResult = null;
        boolean checker = true;
        try {
            registerResult =
                    USER_SERVICE.registerUser(new RegisterRequest("boringusername", "boringpassword", "boringemail@hotmail.com"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        if (switcher.equals("good")) {
            try {
                USER_SERVICE.logoutUser(new LogoutRequest(registerResult.authToken()));
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
                checker = false;
            }
        }
        if (switcher.equals("bad")) {
            try {
                USER_SERVICE.logoutUser(new LogoutRequest("randomstring"));
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
        USER_SERVICE = new UserService();
        GAME_SERVICE = new GameService();
        String authToken = null;
        CreateResult createResult = null;
        try {
            authToken = USER_SERVICE.loginUser(new LoginRequest("username", "password")).authToken();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        try {
            createResult = GAME_SERVICE.createGame(new CreateRequest(authToken,"Name"));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        String expected = "2";
        assertEquals(expected, createResult.gameID());
    }

    @Test
    void badCreateGame() {
        USER_SERVICE = new UserService();
        GAME_SERVICE = new GameService();
        CreateResult createResult = null;
        try {
            createResult = GAME_SERVICE.createGame(new CreateRequest("badtoken","Name"));
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
        CLEAR_SERVICE = new ClearService();
        try {
            result = CLEAR_SERVICE.clear();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        ClearResult expectedClearResult = new ClearResult();
        assertEquals(expectedClearResult, result);
    }
}
