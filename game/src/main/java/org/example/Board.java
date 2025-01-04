package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Board {
    private static int boardIdCounter = 0;

    private int boardId;
    private int playersAmount;

    private int cardAmount = 5;
    private List<Player> players = new ArrayList<>();
    private Deck deck;
    private Player firstPlayer;
    private Player currentPlayer;
    private int currentPlayerIndex;

    private long pot;
    private long currentBet;

    private int round;

    public Board(int amount) {
        this.playersAmount = amount;
        this.boardId = boardIdCounter++;
    }

    public void start() {
        this.deck = Deck.sortedDeck(); // create new sorted deck
        this.deck.shuffle(); // shuffle deck

        for (Player player : players) {
            player.setState(State.WAITING);
        }

        this.currentPlayerIndex = randInt(players.size());
        this.firstPlayer = players.get(randInt(players.size()));
        this.currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setState(State.TURN);

        this.pot = 0;
        this.currentBet = 0;

        this.round = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setState(State.WAITING_FOR_GAME);

        if (players.size() == playersAmount) {
            start();
        }
    }

    public void dealCardsToPlayers() {
        for (Player player : players) { // draw cards to players
            player.drawCards(this.deck.dealCards(cardAmount));
        }
    }

    public List<Card> dealCards(int numberOfCards) {
        return deck.dealCards(numberOfCards);
    }

    public String placeBet(Player player, long amount) {
        if (amount >= currentBet && amount <= player.getMoney()) {
            pot += amount - player.getBet();
            player.setBet(amount);
            currentBet = amount;
            player.setState(State.BET);
            if(amount > currentBet) {
                restartPlayersState();
            }
            return "Placed bet";
        } else if (amount < currentBet){
            return "Bet must be at least " + currentBet + " or higher!";
        } else {
            return "Not enough money!";
        }
    }

    public String call(Player player) {
        if (currentBet == player.getBet()) {
            player.setState(State.CALL);
            return "Call.";
        } else {
            return "You cannot call; there is a bet of " + currentBet;
        }
    }

    public String fold(Player player) {
        player.setState(State.FOLD);
        return "Folds.";
    }

    public void nextPlayer() {
        if (getActivePlayerCount()>1){
            do {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                currentPlayer = players.get(currentPlayerIndex);
                currentPlayer.setState(State.TURN);
            } while (currentPlayer.getState() == State.FOLD || currentPlayer.getState() == State.CALL);
        }
    }

    public boolean isExchangeFinished() {
        for (Player player : players) {
            if (player.getState() != State.EXCHANGE && player.getState() != State.FOLD){
                return false;
            }
        }
        System.out.println("Exchange finished");
        return true;
    }

    public boolean isRoundFinished() {
        for (Player player : players) {
            if (!player.getState().equals(State.FOLD) && !player.getState().equals(State.CALL)) {
                return false;
            }
        }
        return true;
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
            if (!player.getState().equals(State.FOLD)){
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
            if (!player.getState().equals(State.FOLD)) {
                if (currentBet == -1) {
                    currentBet = player.getBet();
                } else if (player.getBet() != currentBet) {
                    return false;
                }
            }
        }

        return true;
    }

    public void restartPlayersState(){
        for (Player player : players) {
            if (!player.getState().equals(State.FOLD)){
                player.setState(State.BET);
            }
        }
    }

    public boolean checkIfAllPlayersAreChecking(){
        for (Player player : players) {
            if (player.getState() != State.CALL && player.getState() != State.FOLD){
                return false;
            }
        }
        System.out.println("All players are checking!");
        return true;
    }


    public int getActivePlayerCount() {
        return (int) players.stream().filter(player -> !player.getState().equals(State.FOLD)).count();
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
