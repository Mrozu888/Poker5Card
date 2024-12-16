package org.example;

import java.nio.channels.SocketChannel;
import java.util.Map;

import static org.example.PokerServer.games;
import static org.example.PokerServer.players;

public class GameAction {

    public Game findPlayersGame(Player player) {
        synchronized (games) {
            for (Game game : games) {
                if (game.getPlayers().contains(player)) {
                    return game;
                }
            }
        }
        return null;
    }

    public void cleanupPlayer(int playerId) {
        // Remove player from active games
        synchronized (games) {
            for (Game game : games) {
                game.removePlayer(players.get(playerId));
            }
        }
        // Remove player from the system
        players.remove(playerId);
        System.out.println("Player with ID " + playerId + " has been removed.");
    }

    public static SocketChannel getSocketChannelByPlayerId(int playerId) {
        return players.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getId() == playerId)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
