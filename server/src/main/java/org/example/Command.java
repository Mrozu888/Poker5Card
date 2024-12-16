package org.example;

import static org.example.PokerServer.games;

public class Command {
    public static int gameIdCounter = 0;

    public static String process(Player player, String command) {
        if (command.equalsIgnoreCase("create")) {
            // Create a new game
            Game game = new Game(gameIdCounter++);
            games.add(game);
            game.addPlayer(player);
            return "Game created with ID: " + game.getGameId();
        } else if (command.startsWith("join")) {
            // Join an existing game
            try {
                int gameId = Integer.parseInt(command.split(" ")[1]);
                Game game;
                game = games.get(gameId);
                if (game == null) return "Game ID " + gameId + " does not exist.";
                game.addPlayer(player);
                return "You have joined game " + gameId;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                return "Invalid command. Use: join <gameId>";
            }
        } else if (command.equalsIgnoreCase("list")) {
            // List all games
            StringBuilder sb = new StringBuilder("Available games:\n");
            for (Game game : games) {
                sb.append("Game ID: ").append(game.getGameId())
                        .append(", Players: ").append(game.getPlayers().size()).append("\n");
            }
            return sb.toString();
        } else if (command.equalsIgnoreCase("checkgames")) {
            // List games with detailed player info
            StringBuilder sb = new StringBuilder("Server Games Status:\n");
            for (Game game : games) {
                sb.append(game.toString()).append("\n");
            }
            return sb.toString();
        } else {
            return "Unknown command. Available commands: create, join <gameId>, list, status, checkgames, exit";
        }
    }
}
