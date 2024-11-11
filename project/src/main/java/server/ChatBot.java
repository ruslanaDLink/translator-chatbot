package server;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ChatBot {
    private static final Logger LOGGER = Logger.getLogger(ChatBot.class.getName());

    private static final int PORT = 5000;
    public static final String NAME = "TranslatorBot";
    private static final Translate translation;

    static {
        translation = TranslateOptions.getDefaultInstance().getService();
    }

    public ChatBot() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("ChatBotServer is running... Waiting for a client to connect...");

            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                System.out.println("Connection established!");
                output.println("Welcome to the Translation ChatBot!");

                String message;
                while ((message = input.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    String response = translate(message, "es"); //spanish
                    output.println(response);
                }
            }
        } catch (IOException e) {
            LOGGER.warning("SERVER REQUEST ERROR");
            LOGGER.warning(e.getMessage());
        }
    }

    public static String translate(String text, String targetLanguage) {
        String translatedText = "";
        if (text == null || text.isBlank()) {
            return "Write text for translation. Specify alphabetic characters only.";
        }
        if (targetLanguage == null || targetLanguage.isBlank()) {
            return text;
        }
        try {
            String language = translation.detect(text).getLanguage();
            System.out.println("Translating from " + language + " to " + targetLanguage + "...");
            Thread.sleep(1000);
            Translation t = translation.translate(text,
                    Translate.TranslateOption.sourceLanguage(language),
                    Translate.TranslateOption.targetLanguage(targetLanguage));
            translatedText = t.getTranslatedText();
        } catch (Exception e) {
            LOGGER.warning("Unable to translate text. \n" + text + "\n Please, try again.");
        }
        return translatedText;
    }

    public static void main(String[] args) {
        new ChatBot();
    }
}
