package org.example;

import java.util.List;

public class Board {
    private int cardAmount = 5;
    private List<Player> players;
    private Deck deck;

    public Board(List<Player> players) {
        this.players = players;
    }

    public void start(){
        this.deck = Deck.sortedDeck(); // create new sorted deck
        this.deck.shuffle(); // shuffle deck


        for (Player player : players) { // draw cards to players
            player.drawCards(this.deck.dealCards(cardAmount));
        }



    }

    public void test(){
        for (Player player : players) {
            int[] indexes = new int[]{0, 1};

            player.exchangeCards(indexes, deck.dealCards(indexes.length));
        }
    }


    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Player player : players) {
            out.append(player.toString()).append("\n");
        }
        return out.toString();
    }
}
