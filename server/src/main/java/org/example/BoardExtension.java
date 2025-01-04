package org.example;

import java.nio.channels.SocketChannel;
import java.util.Map;

import static org.example.PokerServer.*;

public class BoardExtension {
    private static int gameId = 0;

    public static Board findPlayersBoard(Player player) {
        for (Board board : boards) {
            if (board.getPlayers().contains(player)) {
                return board;
            }
        }
        return null;
    }

    public static SocketChannel getSocketChannelByPlayerId(int playerId) {
        return players.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getId() == playerId)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }


    public static Player getPlayerById(int id){
        return players.get(id);
    }
}
