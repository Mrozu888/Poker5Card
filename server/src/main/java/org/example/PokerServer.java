package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class PokerServer {
    private static final int SERVER_PORT = 12345;
    private static final Map<Integer, Game> games = new HashMap<>();
    private static final Map<SocketChannel, Player> players = new HashMap<>();
    private static long playerIdCounter = 0;

    public static void main(String[] args) {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(SERVER_PORT));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Poker Server is listening on port " + SERVER_PORT);

            while (true) {
                selector.select();

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()){
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isAcceptable()) {
                        handleAccept(serverChannel, selector);
                    } else if (key.isReadable()) {
                        handleRead((SocketChannel) key.channel());
                    }
                }

                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleAccept(ServerSocketChannel serverChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);



        System.out.println("[Server]: New player connected.");

        ByteBuffer buffer = ByteBuffer.allocate(256);
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            disconnectPlayer(clientChannel);
            return;
        }
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        String name = new String(data).trim();

        // New player
        Player player;
        if (name.isEmpty()) {
            player = new Player(playerIdCounter++, "Anonymous", 1000);
        }
        else{
            player = new Player(playerIdCounter++, name, 1000);
        }
        players.put(clientChannel, player);

        sendMessage(clientChannel, "Welcome to the Poker Server "+name+"! Type 'list' for available games.");
    }

    private static void handleRead(SocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                disconnectPlayer(clientChannel);
                return;
            }

            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);

            String command = new String(data).trim();
            if (!command.isEmpty()) {
                Player player = players.get(clientChannel);
                String response = processCommand(player, command);
                sendMessage(clientChannel, response);
            }
        } catch (IOException e) {
            disconnectPlayer(clientChannel);
        }
    }

    private static String processCommand(Player player, String command) {
            if (command.equalsIgnoreCase("create")) {
                Game game = new Game();
                games.put(game.getGameId(), game);
                game.addPlayer(player);
                return "Game created with ID: " + game.getGameId();
            } else if (command.startsWith("join")) {
                int gameId = Integer.parseInt(command.split(" ")[1]);
                Game game = games.get(gameId);
                if (game == null) return "Game ID " + gameId + " does not exist.";
                game.addPlayer(player);
                return "You have joined game " + gameId;
            } else if (command.equalsIgnoreCase("list")) {
                StringBuilder sb = new StringBuilder("Available games:\n");
                for (Game game : games.values()) {
                    sb.append("Game ID: ").append(game.getGameId())
                            .append(", Players: ").append(game.getPlayers().size()).append("\n");
                }
                return sb.toString();
            } else if (command.equalsIgnoreCase("status")) {
                return "Your status: " + player;
            } else if (command.equalsIgnoreCase("exit")) {
                return "Goodbye!";
            } else {
                return "Unknown command.";
            }

    }

    private static void sendMessage(SocketChannel clientChannel, String message) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap((message + "\n").getBytes());
            clientChannel.write(buffer);
        } catch (IOException e) {
            disconnectPlayer(clientChannel);
        }
    }

    private static void disconnectPlayer(SocketChannel clientChannel) {
        try {
            Player player = players.remove(clientChannel);
            System.out.println("[Server]: Player disconnected.");
            clientChannel.close();
        } catch (IOException e) {
            System.err.println("Error while disconnecting player: " + e.getMessage());
        }
    }
}
