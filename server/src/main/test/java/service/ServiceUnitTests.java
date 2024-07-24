package service;

import Requests.RegisterRequest;
import Responses.ClearResult;
import Responses.RegisterResult;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserInterface;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceUnitTests {
    ClearService clearService;
    GameService gameService;
    UserService userService;

    @Test
    void goodRegister() {
        userService = new UserService();
        try {
            RegisterResult result = userService.registerUser(new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com"));
            assertNotNull(result.authToken());
            //String expectedUsername = "2kewl";
            //String expectedPassword = "thecooliest";
            //String expectedEmail = "notarealemail@hotmail.com";
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    /*@Test
    void badRegister() {
        userService = new UserService();
        RegisterResult result;
        try {
            assertDoesNotThrow(DataAccessException.class,
                    result = userService.registerUser(new RegisterRequest("2kewl", "thecooliest", "notarealemail@hotmail.com")));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        assertThrows(DataAccessException.class,
                userService.registerUser(new RegisterRequest("2kewl", "evencoolier", "stillnotarealemail@hotmail.com")));
    }*/

    /*@Test
    void clear() {
        goodRegister();
        Set<UserData> expectedUserData = new HashSet<>();
        Set<AuthData> expectedAuthData = new HashSet<>();
        Set<GameData> expectedGameData = new HashSet<>();

        clearService = new ClearService();
        try {
            ClearResult result = clearService.clear();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        //assertEquals(expectedUserData, MemoryUserDAO.getUsers());
    }*/
}
