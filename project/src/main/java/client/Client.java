package client;

import server.ChatBot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 5000;

    public Client() {
        try (Socket clientSocket = new Socket(ADDRESS, PORT);
             InputStreamReader inputReader = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputReader)) {

            System.out.println("Connected to the ChatBot!");
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println(bufferedReader.readLine());
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String message;
            while (true) {
                System.out.print("You: ");
                message = userInput.readLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                printWriter.println(message);
                String response = bufferedReader.readLine();
                if (response != null) {
                    System.out.println(ChatBot.NAME + ": " + response);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("failed connecting chatbot...");
            LOGGER.warning(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
