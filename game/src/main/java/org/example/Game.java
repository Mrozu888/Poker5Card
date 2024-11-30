package org.example;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public long playerId = 0;

    public long gameId = 0;

    public Game(){
        open();
    }
    public void open(){
        Player player1 = new Player(playerId++,"Kamil", 100);
        Player player2 = new Player(playerId++,"Piotrek", 120);
        Player player3 = new Player(playerId++,"Olek", 80);
        Player player4 = new Player(playerId++,"Dominik", 1000);

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

}
