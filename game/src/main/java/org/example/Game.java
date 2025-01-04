package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter
public class Game {


    public static int gameIdCounter = 0;

    private int playersAmount = 2;
    private final int gameId;
    private final List<Player> players;
    private Board board;

    public Game(int gameId) {
        this.gameId = gameId;
        this.players = new ArrayList<>();
        board = new Board(2);
    }
    public void addPlayer(Player player) {
        players.add(player);
        player.setState(State.WAITING_FOR_GAME);

        if (players.size() == playersAmount) {
            start();
        }
    }

    public void removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
        }
    }

    public void start() {
        setupPlayers();

        board.start();

        System.out.println("Starting Poker Game!");
        System.out.println(board);

        betRound();

        board.dealCardsToPlayers();
        System.out.println(board);

        betRound();

        exchangeRound();

        System.out.println(board);

        betRound();

//        board.endRound();
    }
    public void setupPlayers() {
        for (Player player : players) {
            player.setState(State.WAITING);
        }
//        sendInformation(players);
    }

    private void betRound() {
        while (!isRoundOver()){
//            handlePlayerBet(board);
        }
        board.restartPlayersState();
    }

//    private static void sendInformation(List<Player> players) {
//        String message = "Game is starting! \n ";
//        for (Player player : players) {
//            Handlers.send(BoardExtension.getSocketChannelByPlayerId(player.getId()),message);
//        }
//    }

    private void handlePlayerExchange(Board board) {
        Player player = board.getCurrentPlayer();

        System.out.println("\n" + player.getName() + "'s turn to exchange cards!");
        System.out.println("Your hand: " + player.getHand());
        System.out.println("Enter the indexes of cards to exchange (comma-separated, e.g., 0,2,4):");

        Scanner scanner = new Scanner(System.in);
        boolean validExchange = false;

        int[] indexes = null;

        while (!validExchange) {
            String input = scanner.nextLine();
            String[] tokens = input.split(",");

            try {
                indexes = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    indexes[i] = Integer.parseInt(tokens[i].trim());
                }
                validExchange = true;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter numbers separated by commas.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid indexes! Make sure the indexes are valid for your hand.");
            }
        }
        List<Card> newCards = board.dealCards(indexes.length);
        player.exchangeCards(indexes, newCards);

        System.out.println("New hand: " + player.getHand());
        board.nextPlayer();

    }


    private void exchangeRound(){
        while (!board.isExchangeFinished()){
            handlePlayerExchange(board);
        }
    }

    private boolean isRoundOver() {
        if (board.checkIfAllPlayersAreChecking()){
            return true;
        }
        if (board.getActivePlayerCount() == 1) {
            return true;
        }
        return false;
    }



    @Override
    public String toString() {
        return "PokerGame{" +
                "gameId=" + gameId +
                ", players=" + players +
                '}';
    }
}
