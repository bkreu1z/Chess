package server;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;

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
        return makeRequest("POST", path, loginRequest, null);//I'm not sure how to format the request parameter to fit what it's supposed to look like
    }

    public String register(String username, String password, String email) {
        String path = "/user";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        return makeRequest("POST", path, registerRequest, null);
    }

    public void logout(String username, String authToken) {
        String path = "/session";
        makeRequest("DELETE", path, null, null);
    }

    public String listGames(String authToken) {
        String path = "/game";
        return makeRequest("GET", path, null, null);
    }

    public String createGame(String authToken, String gameName) {
        String path = "/game";
        return makeRequest("POST", path, null, null);
    }

    public void joinGame(String authToken, String gameID, String username, String playerColor) {
        String path = "/game";
        makeRequest("PUT", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
            try (OutputStream os = http.getOutputStream()) {//this is throwing an exception, something about the connection being denied
                os.write(reqData.getBytes("UTF-8"));
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (status != 200) {
            throw new IOException(http.getResponseMessage());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
