package org.example;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static int id = 0;
    private final int gameId;
    private List<Player> players;

    public Game(){
//        open();
        gameId = id++;
        players = new ArrayList<>();
    }
    public void open(){
        Player player1 = new Player(0,"Kamil", 100);
        Player player2 = new Player(1,"Piotrek", 120);
        Player player3 = new Player(2,"Olek", 80);
        Player player4 = new Player(3,"Dominik", 1000);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        Board board = new Board(players);
        System.out.println(board);
        board.start();
        System.out.println(board);
        board.test();
        System.out.println(board);

        board.evaluate();

        System.out.println(board.comparePlayersHands());

    }

    public void addPlayer(Player player) {
        if (players.size() >= 4) {
            throw new IllegalStateException("Game is full (max 4 players).");
        }
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public String toString() {
        return "PokerGame{" +
                "gameId=" + gameId +
                ", players=" + players +
                '}';
    }
}
