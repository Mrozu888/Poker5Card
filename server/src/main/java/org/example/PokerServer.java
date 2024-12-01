package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

// PokerServer class to manage multiple games
public class PokerServer {
    private static final int PORT = 12345; // Server port
    private static final Map<Integer, Game> games = new HashMap<>(); // Map of game IDs to game instances
    private static final Map<Socket, Player> playerSockets = new ConcurrentHashMap<>(); // Socket to Player mapping
    private static final ExecutorService threadPool = Executors.newCachedThreadPool(); // Thread pool

    public static void main(String[] args) {
        System.out.println("Poker Server is starting...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                // Accept new client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Handle the client in a separate thread
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    // Inner class to handle client communication
    static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
            ) {
                writer.println("Welcome to the Poker Server! Enter your name:");
                String name = reader.readLine();

                Player player = new Player(playerSockets.size() + 1, name, 1000); // Create a new player
                playerSockets.put(socket, player); // Associate socket with player
                writer.println("You have joined the server. Use 'join <gameId>' or 'create' to start a game.");

                String command;
                while ((command = reader.readLine()) != null) {
                    System.out.println("Received from " + name + ": " + command);

                    if (command.equalsIgnoreCase("exit")) {
                        writer.println("Goodbye!");
                        playerSockets.remove(socket);
                        break;
                    }

                    // Process client commands
                    String response = processCommand(player, command);
                    writer.println(response);
                }
            } catch (IOException e) {
                System.err.println("Client disconnected: " + e.getMessage());
                playerSockets.remove(socket);
            }
        }

        private String processCommand(Player player, String command) {
            if (command.equalsIgnoreCase("create")) {
                // Create a new game
                Game game = new Game();
                synchronized (games) {
                    games.put(game.getGameId(), game);
                }
                game.addPlayer(player);
                return "Game created with ID: " + game.getGameId();
            } else if (command.startsWith("join")) {
                // Join an existing game
                try {
                    int gameId = Integer.parseInt(command.split(" ")[1]);
                    Game game;
                    synchronized (games) {
                        game = games.get(gameId);
                    }
                    if (game == null) return "Game ID " + gameId + " does not exist.";
                    game.addPlayer(player);
                    return "You have joined game " + gameId;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    return "Invalid command. Use: join <gameId>";
                }
            } else if (command.equalsIgnoreCase("list")) {
                // List all games
                StringBuilder sb = new StringBuilder("Available games:\n");
                synchronized (games) {
                    for (Game game : games.values()) {
                        sb.append("Game ID: ").append(game.getGameId())
                                .append(", Players: ").append(game.getPlayers().size()).append("\n");
                    }
                }
                return sb.toString();
            } else if (command.equalsIgnoreCase("status")) {
                // Show game status
                Game game = findPlayerGame(player);
                if (game == null) return "You are not in a game.";
                return "Game status: " + game;
            } else {
                return "Unknown command. Available commands: create, join <gameId>, list, status, exit";
            }
        }

        private Game findPlayerGame(Player player) {
            synchronized (games) {
                for (Game game : games.values()) {
                    if (game.getPlayers().contains(player)) {
                        return game;
                    }
                }
            }
            return null;
        }
    }
}
