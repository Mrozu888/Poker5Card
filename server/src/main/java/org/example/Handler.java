package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static org.example.PokerServer.players;

public class Handler {
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
            System.out.println("Received nickanme: " + message);
            Player player = new Player(playerIdCounter++,message,1000);
            players.put(clientChannel, player);
            buffer.clear();
            buffer.put(("Welcome " + message).getBytes());
            buffer.flip();
        }
        else{
            System.out.println("Received: " + message);

            // Echo the message back to the client
            String response = Command.process(players.get(clientChannel),message);

            buffer.clear();
            buffer.put((response).getBytes());
            buffer.flip();
        }

        clientChannel.write(buffer);
    }
}
