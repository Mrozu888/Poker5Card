//package org.example;
//
//import java.io.*;
//import java.net.*;
//import java.util.*;
//import java.util.concurrent.*;
//
//public class PokerServer {
//    private static final int PORT = 12345; // Server port
//    private static final Map<Integer, Game> games = new HashMap<>(); // Map of game IDs to game instances
//    private static final Map<Integer, Player> players = new ConcurrentHashMap<>(); // Map of player IDs to Player objects
//    private static final Map<Socket, Integer> socketToPlayerId = new ConcurrentHashMap<>(); // Socket to player ID mapping
//    private static final ExecutorService threadPool = Executors.newCachedThreadPool(); // Thread pool
//    private static int playerIdCounter = 1; // Counter for generating unique player IDs
//
//    public static void main(String[] args) {
//        System.out.println("Poker Server is starting...");
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Server is listening on port " + PORT);
//
//            while (true) {
//                // Accept new client connections
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
//
//                // Handle the client in a separate thread
//                threadPool.execute(new ClientHandler(clientSocket));
//            }
//        } catch (IOException e) {
//            System.err.println("Server exception: " + e.getMessage());
//        }
//    }
//
//    // Inner class to handle client communication
//    static class ClientHandler implements Runnable {
//        private final Socket socket;
//
//        public ClientHandler(Socket socket) {
//            this.socket = socket;
//        }
//
//        @Override
//        public void run() {
//            try (
//                    InputStream input = socket.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                    OutputStream output = socket.getOutputStream();
//                    PrintWriter writer = new PrintWriter(output, true);
//            ) {
//                // Assign a unique ID and create a Player
//                int playerId = assignUniqueId();
//                writer.println("Welcome to the Poker Server! Your Player ID is: " + playerId);
//                writer.println("Enter your name:");
//                String name = reader.readLine();
//                Player player = new Player(playerId, name, 1000);
//                players.put(playerId, player);
//                socketToPlayerId.put(socket, playerId);
//
//                writer.println("You have joined the server. Use 'join <gameId>' or 'create' to start a game.");
//
//                String command;
//                while ((command = reader.readLine()) != null) {
//                    System.out.println("Received from " + name + ": " + command);
//
//                    if (command.equalsIgnoreCase("exit")) {
//                        writer.println("Goodbye!");
//                        cleanupPlayer(playerId);
//                        break;
//                    }
//
//                    // Process client commands
//                    String response = processCommand(player, command);
//                    writer.println(response);
//                }
//            } catch (IOException e) {
//                System.err.println("Client disconnected: " + e.getMessage());
//                Integer playerId = socketToPlayerId.get(socket);
//                if (playerId != null) {
//                    cleanupPlayer(playerId);
//                }
//            }
//        }
//
//        private String processCommand(Player player, String command) {
//            if (command.equalsIgnoreCase("create")) {
//                // Create a new game
//                Game game = new Game();
//                synchronized (games) {
//                    games.put(game.getGameId(), game);
//                }
//                game.addPlayer(player);
//                return "Game created with ID: " + game.getGameId();
//            } else if (command.startsWith("join")) {
//                // Join an existing game
//                try {
//                    int gameId = Integer.parseInt(command.split(" ")[1]);
//                    Game game;
//                    synchronized (games) {
//                        game = games.get(gameId);
//                    }
//                    if (game == null) return "Game ID " + gameId + " does not exist.";
//                    game.addPlayer(player);
//                    return "You have joined game " + gameId;
//                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
//                    return "Invalid command. Use: join <gameId>";
//                }
//            } else if (command.equalsIgnoreCase("list")) {
//                // List all games
//                StringBuilder sb = new StringBuilder("Available games:\n");
//                synchronized (games) {
//                    for (Game game : games.values()) {
//                        sb.append("Game ID: ").append(game.getGameId())
//                                .append(", Players: ").append(game.getPlayers().size()).append("\n");
//                    }
//                }
//                return sb.toString();
//            } else if (command.equalsIgnoreCase("status")) {
//                // Show game status
//                Game game = findPlayerGame(player);
//                if (game == null) return "You are not in a game.";
//                return "Game status: " + game;
//            } else if (command.equalsIgnoreCase("checkgames")) {
//                // List games with detailed player info
//                StringBuilder sb = new StringBuilder("Server Games Status:\n");
//                synchronized (games) {
//                    for (Game game : games.values()) {
//                        sb.append(game.toString()).append("\n");
//                    }
//                }
//                return sb.toString();
//            } else {
//                return "Unknown command. Available commands: create, join <gameId>, list, status, checkgames, exit";
//            }
//        }
//
//        private Game findPlayerGame(Player player) {
//            synchronized (games) {
//                for (Game game : games.values()) {
//                    if (game.getPlayers().contains(player)) {
//                        return game;
//                    }
//                }
//            }
//            return null;
//        }
//
//        private synchronized int assignUniqueId() {
//            return playerIdCounter++;
//        }
//
//        private void cleanupPlayer(int playerId) {
//            // Remove player from active games
//            synchronized (games) {
//                for (Game game : games.values()) {
//                    game.removePlayer(players.get(playerId));
//                }
//            }
//            // Remove player from the system
//            players.remove(playerId);
//            socketToPlayerId.remove(socket);
//            System.out.println("Player with ID " + playerId + " has been removed.");
//        }
//    }
//}
