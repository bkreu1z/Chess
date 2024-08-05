package server;

import com.google.gson.Gson;
import requests.CreateRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String login(String username, String password) {
        String path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult result = (LoginResult)makeRequest("POST", path, loginRequest, LoginResult.class, null);
        if (result == null) {
            return null;
        }
        return result.authToken();
    }

    public String register(String username, String password, String email) {
        String path = "/user";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult result = (RegisterResult)makeRequest("POST", path, registerRequest, RegisterResult.class, null);
        if (result == null) {
            return null;
        }
        return result.authToken();
    }

    public void logout(String authToken) {
        String path = "/session";
        makeRequest("DELETE", path, null, LogoutResult.class, authToken);
    }

    public String listGames(String authToken) {//and this one
        String path = "/game";
        makeRequest("GET", path, null, ListResult.class, authToken);
        return "";
    }

    public void createGame(String authToken, String gameName) {
        String path = "/game";
        CreateRequest request = new CreateRequest(authToken, gameName);
        makeRequest("POST", path, request, CreateResult.class, authToken);
    }

    public void joinGame(String authToken, String gameID, String username, String playerColor) {//this one has a header
        String path = "/game";
        makeRequest("PUT", path, null, null, authToken);
    }

    private Record makeRequest(String method, String path, Object request, Class<? extends Record> responseClass, String header) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(header, http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws Exception {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream os = http.getOutputStream()) {
                os.write(reqData.getBytes("UTF-8"));
            }
        }
    }

    private static void writeHeader(String header, HttpURLConnection http) {
        if (header != null) {
            http.addRequestProperty("Authorization", header);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (status != 200) {
            throw new IOException(http.getResponseMessage());
        }
    }

    private static Record readBody(HttpURLConnection http, Class<?extends Record> responseClass) throws IOException {
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    return new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return null;
    }
}
