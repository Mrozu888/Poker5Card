package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class PokerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the Poker Server!");

            // Read the welcome message
            String serverMessage;
            while ((serverMessage = reader.readLine()) != null) {
                System.out.println(serverMessage);

                // Wait for user input and send to server
                System.out.print("> ");
                String clientMessage = scanner.nextLine();

                writer.println(clientMessage); // Send message to server

                // Break the loop if the client wants to exit
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnecting from server...");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}

