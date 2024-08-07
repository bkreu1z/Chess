package ui;

import ui.websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class Repl implements NotificationHandler {
    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to Chess! Log in to start");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print("\n>>>");
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (!result.equals("quit")) {
                    System.out.print(result);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage);
        System.out.print(">>>");
    }
}
