package org.example.commands;

import org.example.Board;
import org.example.Game;
import org.example.Player;

import static org.example.PokerServer.boards;

public class ListCommand implements Command{
    @Override
    public String execute(Player player, String[] args) {
        return switch (player.getState()) {
            case LOBBY, WAITING_FOR_GAME -> listBoards();
            default -> "Wrong operation";
        };
    }

    public static String listBoards(){
        if (boards.isEmpty()) return "No games found";
        StringBuilder sb = new StringBuilder();
        for(Board b : boards){
            sb.append(b.toString());
        }
        return sb.toString();
    }
}
