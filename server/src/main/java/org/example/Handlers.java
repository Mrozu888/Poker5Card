package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static org.example.PokerServer.players;

public class Handlers {
    private static int playerIdCounter = 0;

    public static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);

        clientChannel.register(key.selector(), SelectionKey.OP_READ);
        System.out.println("New client connected: " + clientChannel.getRemoteAddress());
    }

    public static void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);

        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            clientChannel.close();
            System.out.println("Client disconnected.");
            return;
        }

        buffer.flip();
        String message = new String(buffer.array(), 0, buffer.limit()).trim();

        if ("exit".equalsIgnoreCase(message)) {
            System.out.println("Client requested disconnection.");
            clientChannel.close();
            return;
        }

        if (!players.containsKey(clientChannel)) {
            System.out.println("Received nickname: " + message);
            Player player = new Player(playerIdCounter++, message, 1000);
            players.put(clientChannel, player);
            send(clientChannel, "Welcome " + message);
        } else {
            System.out.println("Received: " + message);

            // Parse message using InputManager
            CommandManager.manage(players.get(clientChannel), message);
        }
    }

    /**
     * Sends a message to a specific client.
     *
     * @param clientChannel The client to send the message to.
     * @param message       The message to send.
     * @throws IOException If an I/O error occurs.
     */
    public static void send(SocketChannel clientChannel, String message) {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.put(message.getBytes());
        buffer.flip();
        try {
            clientChannel.write(buffer);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace(); // Optional: log the stack trace for debugging
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The message to broadcast.
     * @throws IOException If an I/O error occurs.
     */
    public static void broadcast(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.put(message.getBytes());
        buffer.flip();

        for (Map.Entry<SocketChannel, Player> entry : players.entrySet()) {
            SocketChannel clientChannel = entry.getKey();
            clientChannel.write(buffer);
            buffer.rewind(); // Rewind buffer to allow reuse for multiple clients
        }
    }
}
