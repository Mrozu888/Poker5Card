package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class PokerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {

        System.out.println("Poker Client is running...");
        System.out.println("Enter your name: ");
        String name = new Scanner(System.in).nextLine();

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
            System.out.println("Connected to Poker Server!");

            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(256);

            buffer.clear();
            buffer.put(name.getBytes());
            buffer.flip();
            socketChannel.write(buffer);


            while (true) {
                System.out.print("> ");
                String userInput = scanner.nextLine();

                if (userInput.trim().isEmpty()) {
                    System.out.println("Please enter a valid command.");
                    continue;
                }

                // Wysyłanie danych do serwera
                buffer.clear();
                buffer.put(userInput.getBytes());
                buffer.flip();
                socketChannel.write(buffer);

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Disconnecting from server...");
                    break;
                }

                // Odbieranie odpowiedzi od serwera
                buffer.clear();
                int bytesRead = socketChannel.read(buffer);
                if (bytesRead > 0) {
                    buffer.flip();
                    String serverResponse = new String(buffer.array(), 0, buffer.limit()).trim();
                    System.out.println("[Server]: " + serverResponse);
                }
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
