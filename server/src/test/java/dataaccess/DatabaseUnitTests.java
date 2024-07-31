package dataaccess;

import requests.*;
import responses.*;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;

import java.sql.Connection;

public class DatabaseUnitTests {
    AuthInterface authDAO = new SQLAuthDAO();
    UserInterface userDAO = new SQLUserDAO();
    GameInterface gameDAO = new SQLGameDAO();

    @Test
    void goodCreateAuth() {
        String token = makeTestAuth("goodCreateAuth");
        String actualUsername = "";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, authusername FROM auth WHERE token = \"" + token + "\"";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        actualUsername = rs.getString("authusername");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertEquals(actualUsername, "goodCreateUsername");
        authDAO.clear();
    }

    @Test
    void badCreateAuth() {
        String token = makeTestAuth("badCreateAuth");
        String actualUsername = "";
        boolean checker = false;
        authDAO.clear();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, authusername FROM auth WHERE token = \"" + token + "\"";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        checker = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertFalse(checker);
    }

    @Test
    void goodGetAuth() {
        String token = makeTestAuth("goodGetAuth");
        try {
            Assertions.assertTrue(authDAO.getAuth(token));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        authDAO.clear();
    }

    @Test
    void badGetAuth() {
        String token = makeTestAuth("badGetUsername");
        authDAO.clear();
        try {
            Assertions.assertFalse(authDAO.getAuth(token));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void goodGetUser() {
        String ogUsername = "goodGetUsername";
        String token = makeTestAuth(ogUsername);
        String actualUsername = "";
        try {
            actualUsername = authDAO.getUsername(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertEquals(actualUsername, ogUsername);
        authDAO.clear();
    }

    @Test
    void badGetUser() {
        String ogUsername = "badGetUsername";
        String token = makeTestAuth(ogUsername);
        String actualUsername = "";
        authDAO.clear();
        try {
            actualUsername = authDAO.getUsername(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertNotEquals(actualUsername, ogUsername);
    }

    @Test
    void goodDeleteAuth() {
        String ogUsername = "goodDeleteUsername";
        String token = makeTestAuth(ogUsername);
        String actualUsername = "";
        authDAO.deleteAuth(token);
        try {
            actualUsername = authDAO.getUsername(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertNotEquals(actualUsername, ogUsername);
    }

    @Test
    void badDeleteAuth() {
        String ogUsername = "badDeleteUsername";
        String token = makeTestAuth(ogUsername);
        String actualUsername = "";
        authDAO.deleteAuth("randomToken");
        try {
            actualUsername = authDAO.getUsername(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertEquals(actualUsername, ogUsername);
    }

    @Test
    void clearAuth() {
        String aToken = makeTestAuth("a");
        String bToken = makeTestAuth("b");
        String cToken = makeTestAuth("c");
        authDAO.clear();
        try {
            Assertions.assertFalse(authDAO.getAuth(aToken));
            Assertions.assertFalse(authDAO.getAuth(bToken));
            Assertions.assertFalse(authDAO.getAuth(cToken));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String makeTestAuth(String username) {
        String token = "";
        try {
            token = authDAO.createAuth(username);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return token;
    }
}
