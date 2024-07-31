package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserInterface {

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to update database");
        }
    }

    @Override
    public boolean createUser(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        var id = executeUpdate(statement, username, hashedPassword, email);
        return id > 0;
    }

    @Override
    public boolean getUser(String username) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, username FROM users WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to get user");
        }
        return false;
    }

    @Override
    public Set<UserData> getUsers() throws DataAccessException {
        Set<UserData> users = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        users.add(new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email")));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not get users");
        }
        return users;
    }

    @Override
    public boolean verifyPassword(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id FROM users WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String dbpassword = rs.getString("password");
                        return BCrypt.checkpw(password, dbpassword);
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        var statement = "DELETE FROM users WHERE username = ?";
        try {
            executeUpdate(statement);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        var statement = "DELETE FROM users";
        try {
            executeUpdate(statement);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
