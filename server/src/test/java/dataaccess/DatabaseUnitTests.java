package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        authDAO.clear();
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

    @Test
    void goodCreateUser() {
        String ogUsername = "goodCreateUsername";
        String actualUsername = getUsernameInDB(ogUsername);
        makeTestUser(ogUsername);
        Assertions.assertEquals(actualUsername, ogUsername);
        userDAO.clear();
    }

    @Test
    void badCreateUser() {
        String ogUsername = "badCreateUsername";
        String actualUsername = getUsernameInDB(ogUsername);
        makeTestUser(ogUsername);
        userDAO.clear();
        Assertions.assertNotEquals(actualUsername, ogUsername);
    }

    @Test
    void goodCheckUserPresent() {
        String ogUsername = "goodUsernamePresent";
        makeTestUser(ogUsername);
        try {
            Assertions.assertTrue(userDAO.getUser(ogUsername));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        userDAO.clear();
    }

    @Test
    void badCheckUserPresent() {
        String ogUsername = "badUsernamePresent";
        makeTestUser(ogUsername);
        userDAO.clear();
        try {
            Assertions.assertFalse(userDAO.getUser(ogUsername));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void goodGetUsers() {
        Set<UserData> users = new HashSet<>();
        Set<UserData> actualUsers = new HashSet<>();
        ArrayList<String> usernames = new ArrayList<>();
        makeTestUser("x");
        usernames.add("x");
        makeTestUser("y");
        usernames.add("y");
        makeTestUser("z");
        usernames.add("z");
        String hashedPassword = "password";
        hashedPassword = BCrypt.hashpw(hashedPassword, BCrypt.gensalt());
        users.add(new UserData("x", hashedPassword, "email@byu.edu"));
        users.add(new UserData("y", hashedPassword, "email@byu.edu"));
        users.add(new UserData("z", hashedPassword, "email@byu.edu"));
        try {
            actualUsers = userDAO.getUsers();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertNotNull(actualUsers);
        int i = 0;
        for (UserData user : actualUsers) {
            Assertions.assertEquals(user.username(), usernames.get(i));
            i++;
        }
        userDAO.clear();
    }

    @Test
    void badGetUsers() {
        Set<UserData> users = new HashSet<>();
        Set<UserData> actualUsers = new HashSet<>();
        makeTestUser("t");
        makeTestUser("w");
        makeTestUser("s");
        String hashedPassword = "password";
        hashedPassword = BCrypt.hashpw(hashedPassword, BCrypt.gensalt());
        users.add(new UserData("t", hashedPassword, "email@byu.edu"));
        users.add(new UserData("w", hashedPassword, "email@byu.edu"));
        users.add(new UserData("s", hashedPassword, "email@byu.edu"));
        userDAO.clear();
        try {
            actualUsers = userDAO.getUsers();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertTrue(actualUsers.isEmpty());
    }

    @Test
    void goodVerifyPassword() {
        String ogUsername = "goodVerifyPassword";
        makeTestUser(ogUsername);
        Assertions.assertTrue(userDAO.verifyPassword(ogUsername, "password"));
        userDAO.clear();
    }

    @Test
    void badVerifyPassword() {
        String ogUsername = "badVerifyPassword";
        makeTestUser(ogUsername);
        userDAO.clear();
        Assertions.assertFalse(userDAO.verifyPassword(ogUsername, "password"));
    }

    @Test
    void goodDeleteUser() {
        String ogUsername = "goodDeleteUser";
        makeTestUser(ogUsername);
        try {
            userDAO.deleteUser(ogUsername);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            Assertions.assertFalse(userDAO.getUser(ogUsername));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void badDeleteUser() {
        String ogUsername = "badDeleteUser";
        makeTestUser(ogUsername);
        try {
            userDAO.deleteUser("otherUsername");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            Assertions.assertTrue(userDAO.getUser(ogUsername));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void userClear() {
        makeTestUser("b");
        makeTestUser("t");
        makeTestUser("s");
        userDAO.clear();
        try {
            Assertions.assertFalse(userDAO.getUser("b"));
            Assertions.assertFalse(userDAO.getUser("t"));
            Assertions.assertFalse(userDAO.getUser("s"));
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

    private void makeTestUser(String username) {
        try {
            userDAO.createUser(username, "password", "email@byu.edu");
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getUsernameInDB(String username) {
        String actualUsername = "";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, username FROM users WHERE username = \"" + username + "\"";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        actualUsername = rs.getString("username");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return actualUsername;
    }
}
