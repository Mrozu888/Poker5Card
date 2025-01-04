package org.example.commands;

import org.example.Board;
import org.example.Game;
import org.example.Player;

import static org.example.Game.gameIdCounter;
import static org.example.PokerServer.boards;

public class CreateCommand implements Command {
    @Override
    public String execute(Player player, String[] args) {
        return switch (player.getState()) {
            case LOBBY -> createBoard(player, args);
            default -> "Wrong operation";
        };
    }

    public static String createBoard(Player player, String[] args){
        if (args.length != 2) {
            return "Wrong syntax";
        }
        // Try to parse the name as an integer
        try {
            int amount = Integer.parseInt(args[1]); // Try parsing args[1] to an integer
            if (amount < 2 || amount > 6) {
                return "Wrong player amount";
            }
            Board board = new Board(amount);
            board.addPlayer(player);
            boards.add(board);
            return board.toString();
        } catch (NumberFormatException e) {
            return "Invalid player amount. Please provide a valid integer.";
        }
    }
}
