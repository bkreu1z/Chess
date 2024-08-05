package client;

import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.ClearService;


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

}
