package org.example.commands;

import org.example.Board;
import org.example.Game;
import org.example.Player;

import static org.example.BoardExtension.findPlayersBoard;

public class ExchangeCommand implements Command {
    @Override
    public String execute(Player player, String[] args) {
        return switch (player.getState()) {
            case TURN -> exchange(player, args);
            default -> "Wrong operation";
        };
    }

    public static String exchange(Player player, String[] args) {

        if (args.length != 2) {
            return "Wrong syntax";
        }
        // Try to parse the name as an integer
        try {
            long amount = Integer.parseInt(args[1]); // Try parsing args[1] to an integer
            Board board = findPlayersBoard(player);
            return board.placeBet(player, amount);
        } catch (NumberFormatException e) {
            return "Invalid amount. Please provide a valid integer.";
        }
    }
}
