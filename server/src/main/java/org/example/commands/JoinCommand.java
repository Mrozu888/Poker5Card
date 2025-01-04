package org.example.commands;

import org.example.Board;
import org.example.Handlers;
import org.example.Player;
import org.example.Game;

import static org.example.BoardExtension.getSocketChannelByPlayerId;
import static org.example.PokerServer.boards;

public class JoinCommand implements Command {
    @Override
    public String execute(Player player, String[] args) {
        return switch (player.getState()) {
            case LOBBY -> joinGame(player, args);
            default -> "Wrong operation";
        };
    }

    public String joinGame(Player player, String[] args) {
        if (args.length != 2) {
            return "Wrong game id";
        }
        // Try to parse the name as an integer
        try {
            int id = Integer.parseInt(args[1]); // Try parsing args[1] to an integer

            // Assuming you have a game object and method to add a player
            Board board = getBoardById(id); // You need to implement this method
            if (board != null) {
                board.addPlayer(player);
                String output = "Player: "+ player + "joined the game";
                for (Player p : board.getPlayers()) {
                    Handlers.send(getSocketChannelByPlayerId(p.getId()),output);
                }
                return "Player added to the game with ID " + id;
            } else {
                return "Game not found with ID " + id;
            }
        } catch (NumberFormatException e) {
            return "Invalid game ID. Please provide a valid integer.";
        }
    }

    private Board getBoardById(int id) {
        // Iterate through the list of games and return the game with the matching ID
        for (Board board : boards) {
            if (board.getBoardId() == id) {
                return board;
            }
        }
        return null; // Return null if no game found with the given ID
    }
}
