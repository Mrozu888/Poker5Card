package org.example.commands;

import org.example.Player;

import org.example.*;

import static org.example.BoardExtension.findPlayersBoard;

public class FoldCommand implements Command {

    @Override
    public String execute(Player player, String[] args) {
        return switch (player.getState()) {
            case BET,CALL -> fold(player);
            default -> "Wrong operation"; // Obsługa innych stanów, jeśli istnieją
        };
    }
    private String fold(Player player) {
        Board board = findPlayersBoard(player);
        return board.fold(player);
    }

}
