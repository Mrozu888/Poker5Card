package org.example;

import static org.example.PokerServer.games;

public class Command {
    public static int gameIdCounter = 0;

    public static String process(Player player, String command) {
        switch (player.getState()) {
            case State.LOBBY:
                switch (getCommandName(command).toLowerCase()) {
                    case "create":
                        return create(player);
                    case "join":
                        return join(player, command);
                    case "list":
                        return list();
                    case "checkgames":
                        return checkGames();
                    default:
                        return "Unknown command. Available commands: create, join <gameId>, list, checkgames, exit";
                }
            case State.TURN:
                switch (getCommandName(command).toLowerCase()) {
                    case "bet":
                        return bet();
                    case "raise":
                        return raise();
                    case "check":
                        return check();
                    case "fold":
                        return fold();
                    default:
                        return "Unknown command. Available commands: bet, raise, check, fold, exit";
                }
            default:
                return "Unknown command";
        }
    }

    private static String getCommandName(String command) {
        return command.split("\\s+")[0];
    }

    private static String create(Player player) {
        Game game = new Game(gameIdCounter++);
        games.add(game);
        game.addPlayer(player);
        return "Game created with ID: " + game.getGameId();
    }

    private static String join(Player player, String command) {
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
    }

    private static String list() {
        StringBuilder sb = new StringBuilder("Available games:\n");
        for (Game game : games) {
            sb.append("Game ID: ").append(game.getGameId())
                    .append(", Players: ").append(game.getPlayers().size()).append("\n");
        }
        return sb.toString();
    }

    private static String checkGames() {
        // List games with detailed player info
        StringBuilder sb = new StringBuilder("Server Games Status:\n");
        for (Game game : games) {
            sb.append(game.toString()).append("\n");
        }
        return sb.toString();
    }

    // in game commands
    private static String bet(){
        return "";
    }

    private static String raise(){
        return "";
    }

    private static String exchange(){
        return "";
    }

    private static String check(){
        return "";
    }

    private static String fold(){
        return "";
    }
}
