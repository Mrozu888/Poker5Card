package org.example.commands;

import org.example.*;

import static org.example.BoardExtension.getSocketChannelByPlayerId;
import static org.example.PokerServer.boards;

public class QuitCommand implements Command {
    @Override
    public String execute(Player player, String[] args) {
        return switch (player.getState()) {
            case WAITING_FOR_GAME -> quit(player);
            default -> "Wrong operation";
        };
    }

    private static String quit(Player player) {
        Board board = null;
        for (Board b : boards) {
            for (Player p : b.getPlayers()) {
                if (p == player) {
                    board = b;
                }
            }
        }
        String output = "Player "+ player + " quit the game";
        for (Player p : board.getPlayers()) {
            if (p == player) {
                Handlers.send(getSocketChannelByPlayerId(p.getId()),output);
            }

        }
        player.setState(State.LOBBY);
        return "Quit";
    }
}
