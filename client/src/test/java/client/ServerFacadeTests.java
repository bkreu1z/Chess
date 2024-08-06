package client;

import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.ClearService;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static ClearService clearService = new ClearService();
    private static SQLAuthDAO authDAO = new SQLAuthDAO();
    private static SQLUserDAO userDAO = new SQLUserDAO();
    private static SQLGameDAO gameDAO = new SQLGameDAO();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        try {
            clearService.clear();
        } catch (Exception e) {
            System.out.println("Failed to clear service");
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void goodRegister() {
        facade.register("goodRegister","passRegister", "email@byu.edu");
        try {
            Assertions.assertTrue(userDAO.getUser("goodRegister"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
    }

    @Test
    public void badRegister() {
        facade.register("goodRegister","passRegister", "email@byu.edu");
        Assertions.assertNull(facade.register("goodRegister","passRegister", "email@byu.edu"));
        userDAO.clear();
    }

    @Test
    public void goodLogout() {
        String authToken = facade.register("goodLogout","passLogout", "email@byu.edu");
        facade.logout(authToken);
        try {
            Assertions.assertFalse(authDAO.getAuth(authToken));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
    }

    @Test
    public void badLogout() {
        String authToken = facade.register("goodLogout","passLogout", "email@byu.edu");
        facade.logout("badtoken");
        try {
            Assertions.assertTrue(authDAO.getAuth(authToken));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    public void goodLogin() {
        String authToken = facade.register("goodLogin","passLogin", "email@byu.edu");
        facade.logout(authToken);
        String newAuthToken = facade.login("goodLogin","passLogin");
        try {
            Assertions.assertTrue(authDAO.getAuth(newAuthToken));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    public void badLogin() {
        String authToken = facade.register("badLogin","passLogin", "email@byu.edu");
        facade.logout(authToken);
        Assertions.assertNull(facade.login("badLogin","wrongPassword"));
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    public void goodCreate() {
        String authToken = facade.register("goodCreate","passCreate", "email@byu.edu");
        facade.createGame(authToken, "NameOfGame");
        try {
            Assertions.assertTrue(gameDAO.getGameByName("NameOfGame"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void badCreate() {
        facade.register("badCreate","passCreate", "email@byu.edu");
        facade.createGame("badToken", "Steven");
        try {
            Assertions.assertFalse(gameDAO.getGameByName("Steven"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    public void goodList() {
        String authToken = facade.register("goodList","passList", "email@byu.edu");
        facade.createGame(authToken, "I");
        facade.createGame(authToken, "Love");
        facade.createGame(authToken, "Pizza");
        ArrayList<GameData> gameData = facade.listGames(authToken);
        Assertions.assertNotNull(gameData);
        ArrayList<String> expectedNames = new ArrayList<>();
        expectedNames.add("I");
        expectedNames.add("Love");
        expectedNames.add("Pizza");
        ArrayList<String> actualNames = new ArrayList<>();
        for (GameData game : gameData) {
            actualNames.add(game.gameName());
        }
        Assertions.assertEquals(expectedNames, actualNames);
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void badList() {
        String authToken = facade.register("badList","passList", "email@byu.edu");
        facade.createGame(authToken, "Computers");
        facade.createGame(authToken, "Are");
        facade.createGame(authToken, "Cool");
        Assertions.assertNull(facade.listGames("badToken"));
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void goodJoin() {
        String authToken = facade.register("goodJoin","passJoin", "email@byu.edu");
        facade.createGame(authToken, "Hyrum");
        String gameID = facade.listGames(authToken).getFirst().gameID();
        facade.joinGame(authToken, gameID, "WHITE");
        try {
            Assertions.assertFalse(gameDAO.checkColorNull("WHITE", gameID));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void badJoin() {
        String authToken = facade.register("badJoin","passJoin", "email@byu.edu");
        facade.createGame(authToken, "Abraham");
        String gameID = facade.listGames(authToken).getFirst().gameID();
        facade.joinGame(authToken, "badID", "WHITE");
        try {
            Assertions.assertTrue(gameDAO.checkColorNull("WHITE", gameID));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

}
