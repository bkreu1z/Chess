package ui;

import java.util.Scanner;

public class Repl {
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
                System.out.print(result);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
