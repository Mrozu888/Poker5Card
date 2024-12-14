package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private int cardAmount = 5;
    private final List<Player> players;
    private Deck deck;
    private Player firstPlayer;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private long pot;
    private long currentBet;
    private int level;

    public Board(List<Player> players) {
        this.players = players;
    }

    public void start() {
        this.deck = Deck.sortedDeck(); // create new sorted deck
        this.deck.shuffle(); // shuffle deck

//        this.dealCardsToPlayers();
        this.currentPlayerIndex = randInt(players.size());
        this.firstPlayer = players.get(currentPlayerIndex);
        this.currentPlayer = firstPlayer;
        this.pot = 0;
        this.currentBet = 0;
    }

    public void dealCardsToPlayers() {
        for (Player player : players) { // draw cards to players
            player.drawCards(this.deck.dealCards(cardAmount));
        }
    }

    public boolean placeBet(long amount) {
        if (amount >= currentBet && currentPlayer.placeBet(amount)) {
            pot += amount;
            currentBet = amount;
            System.out.println(currentPlayer.getName() + " bets " + amount);
            return true;
        } else {
            System.out.println("Bet must be at least " + currentBet + " or higher!");
            return false;
        }
    }

    public boolean raise(long amount) {
        if (amount > currentBet) {
            currentPlayer.placeBet(amount);
            pot += amount-currentPlayer.getBet();
            currentBet = amount;
            System.out.println(currentPlayer.getName() + " raises to " + amount);

            for (Player player : players) {
                if (!player.hasFolded()) {
                    player.setChecking(false);
                }
            }

            return true;
        } else {
            System.out.println("Raise must be greater than the current bet of " + currentBet);
            return false;
        }
    }

    public boolean check() {
        if (currentBet == currentPlayer.getBet()) {
            System.out.println(currentPlayer.getName() + " checks.");
            currentPlayer.setChecking(true);
            return true;
        } else {
            System.out.println("You cannot check; there is a bet of " + currentBet);
            return false;
        }
    }

    public void fold() {
        currentPlayer.fold();
        System.out.println(currentPlayer.getName() + " folds.");
    }

    public void nextPlayer() {
        if (getActivePlayerCount()>1){
            do {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                currentPlayer = players.get(currentPlayerIndex);
            } while (currentPlayer.hasFolded());
        }
    }


    public void endRound() {
        evaluate();
        System.out.println("Round ends. Pot is " + pot);
        List<Player> winners = comparePlayersHands();
        System.out.println("Winners: ");
        for (Player winner : winners) {
            System.out.println(winner.getName());
            winner.addWinnings(pot / winners.size());
        }
        pot = 0; // Reset pot for next round
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
            if (!player.hasFolded()){
                int value = player.getHandValues()[0];
                if (value > bestHand) {
                    playersHands.clear();
                    playersHands.add(player);
                    bestHand = value;
                } else if (value == bestHand) {
                    playersHands.add(player);
                }
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

    public boolean isBettingEqual() {
        long currentBet = -1;

        for (Player player : players) {
            if (!player.hasFolded()) {
                if (currentBet == -1) {
                    currentBet = player.getBet();
                } else if (player.getBet() != currentBet) {
                    return false;
                }
            }
        }

        return true;
    }

    public long getPot() {
        return pot;
    }

    public int getActivePlayerCount() {
        return (int) players.stream().filter(player -> !player.hasFolded()).count();
    }
    public boolean checkIfAllPlayersAreChecking(){
        for (Player player : players) {
            if (!player.isChecking() && !player.hasFolded()){
                return false;
            }
        }
        System.out.println("All players are checking!");
        return true;
    }

    public long getCurrentBet() {
        return currentBet;
    }

    public int getCardAmount() {
        return cardAmount;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
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
        return random.nextInt(max);
    }


}
