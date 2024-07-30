package dataaccess;

public class SQLAuthDAO implements AuthInterface{
    @Override
    public String createAuth(String username) {
        return "";
    }

    @Override
    public boolean getAuth(String token) {
        return false;
    }

    @Override
    public String getUsername(String token) {
        return "";
    }

    @Override
    public boolean deleteAuth(String token) {
        return false;
    }

    @Override
    public void clear() {

    }
}
