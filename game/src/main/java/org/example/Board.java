package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private int cardAmount = 5;
    private final List<Player> players;
    private Deck deck;
    private Player currentPlayer;

    public Board(List<Player> players) {
        this.players = players;
    }

    public void start(){
        this.deck = Deck.sortedDeck(); // create new sorted deck
        this.deck.shuffle(); // shuffle deck

        this.currentPlayer = players.get(randInt(players.size()));
    }

    public void dealCardsToPlayers(){
        for (Player player : players) { // draw cards to players
            player.drawCards(this.deck.dealCards(cardAmount));
        }
    }

    public void playerMove(){

    }

    public void test(){
        for (Player player : players) {
            int[] indexes = new int[]{0, 1};

            player.exchangeCards(indexes, deck.dealCards(indexes.length));
        }
    }

    public void evaluate(){
        for (Player player : players) {
            System.out.println(player.getHand().evaluateHand());
        }
    }
    public List<Player> comparePlayersHands() {
        List<Player> playersHands = new ArrayList<>();
        int bestHand = -1;

        // Find the players with the best hand values
        for (Player player : players) {
            int value = player.getHandValues()[0];
            if (value > bestHand) {
                playersHands.clear();
                playersHands.add(player);
                bestHand = value;
            } else if (value == bestHand) {
                playersHands.add(player);
            }
        }

        // If there's a tie, resolve it using rank values
        if (playersHands.size() > 1) {
            List<Player> winners = new ArrayList<>();
            int bestRank = -1;

            for (Player player : playersHands) {
                int rankValue = player.getHandValues()[1];
                if (rankValue > bestRank) {
                    winners.clear();
                    winners.add(player);
                    bestRank = rankValue;
                } else if (rankValue == bestRank) {
                    winners.add(player);
                }
            }

            return winners;
        }

        // If no ties, return the single winner
        return playersHands;
    }



    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Player player : players) {
            out.append(player.toString()).append("\n");
        }
        return out.toString();
    }

    public static int randInt(int max) {
        Random random = new Random();
        return random.nextInt(max) ;
    }
}
