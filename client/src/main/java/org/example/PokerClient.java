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
        try (SocketChannel clientChannel = SocketChannel.open()) {
            clientChannel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
            System.out.println("Connected to the server.");

            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(256);

            System.out.print("Enter nickname: ");
            String message = scanner.nextLine().trim();

            // Check if the user pressed Enter without typing anything
            if (message.isEmpty()) {
                System.out.println("Nickname cannot be empty. Please enter a valid nickname.");
                return; // Exit or handle as needed
            }

            // Send message to the server
            clientChannel.write(ByteBuffer.wrap(message.getBytes()));

            // Read the server's response
            buffer.clear();
            int bytesRead = clientChannel.read(buffer);
            buffer.flip();
            String response = new String(buffer.array(), 0, buffer.limit()).trim();
            System.out.println(response);

            while (true) {
                System.out.print("> ");
                message = scanner.nextLine().trim();

                // Handle empty input (if the user just presses Enter)
                if (message.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a valid message.");
                    continue; // Skip this loop iteration and prompt again
                }

                // Send message to the server
                clientChannel.write(ByteBuffer.wrap(message.getBytes()));

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Disconnecting...");
                    break;
                }

                // Read the server's response
                buffer.clear();
                bytesRead = clientChannel.read(buffer);
                if (bytesRead == -1) {
                    System.out.println("Server closed the connection.");
                    break;
                }

                buffer.flip();
                response = new String(buffer.array(), 0, buffer.limit()).trim();
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
