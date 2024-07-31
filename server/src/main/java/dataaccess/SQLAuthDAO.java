package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthInterface{

    public static int executeUpdate(String statement, Object... params) throws DataAccessException {
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
    public String createAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        var statement = "INSERT INTO auth (authusername, token) VALUES (?, ?)";
        try {
            executeUpdate(statement, username, token);
        } catch (Exception e) {
            throw new DataAccessException("could not create Authtoken");
        }
        return token;
    }

    @Override
    public boolean getAuth(String token) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id FROM auth WHERE token = '" + token + "'";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not verify auth token");
        }
        return false;
    }

    @Override
    public String getUsername(String token) throws DataAccessException {
        String username = "";
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authusername FROM auth WHERE token = '" + token + "'";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("authusername");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not get username from authtoken");
        }
        return username;
    }

    @Override
    public boolean deleteAuth(String token) {
        var statement = "DELETE FROM auth WHERE token = ?";
        try {
            executeUpdate(statement, token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        var statement = "DELETE FROM auth";
        try {
            executeUpdate(statement);
        } catch (Exception e) {
            System.out.println("could not clear authtoken table");
        }
    }
}
