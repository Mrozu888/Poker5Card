package org.example.commands;

import org.example.Board;
import org.example.Player;

import static org.example.BoardExtension.findPlayersBoard;

public class CallCommand implements Command {

    @Override
    public String execute(Player player, String[] args) {
        return switch (player.getState()) {
            case TURN -> call(player);
            default -> "Wrong operation";
        };
    }
    private String call(Player player) {
        Board board = findPlayersBoard(player);
        return board.call(player);
    }
}
