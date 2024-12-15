package org.example;

import java.util.*;

public class Game {
    private static int id = 0;
    private final int gameId;
    private final List<Player> players;
    private Board board;

    public Game() {
        this.gameId = id++;
        this.players = new ArrayList<>();
        board = new Board(players);
    }
    public void addPlayer(Player player) {
        if (players.size() < 5) {
            players.add(player);
        } else {
            System.out.println("Game is full!");
        }
    }

    public void removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
        } else {
            System.out.println("Player not found!");
        }
    }

    public void start() {
//        setupPlayers();

        board.start();

        System.out.println("Starting Poker Game!");
        System.out.println(board);

        betRound();

        board.dealCardsToPlayers();
        System.out.println(board);

        betRound();

        exchangeRound();

        betRound();

        board.endRound();
    }

    private void setupPlayers() {
        players.add(new Player(0, "Kamil", 100));
        players.add(new Player(1, "Piotrek", 120));
        players.add(new Player(2, "Olek", 80));
        players.add(new Player(3, "Dominik", 1000));
    }

    private void betRound() {
        while (!isRoundOver()){
            handlePlayerBet(board);
        }
        board.restartPlayersState();
    }

    private void handlePlayerBet(Board board) {
        Player player = board.getCurrentPlayer();

        System.out.println("\n" + player.getName() + "'s turn!");
        System.out.println("Current pot: " + board.getPot());
        System.out.println("Current bet to match: " + board.getCurrentBet());
        System.out.println("Your money: " + player.getMoney());

        Scanner scanner = new Scanner(System.in);
        String action = null;
        long amount = 0;

        boolean validAction = false;

        // Loop to validate player input
        while (!validAction ) {
            System.out.println("Choose action: (bet, raise, check, fold)");
            action = scanner.nextLine().toLowerCase();

            if (action.equals("bet") || action.equals("raise")) {
                System.out.print("Enter amount: ");
                if (scanner.hasNextLong()) {
                    amount = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    if (amount <= 0 || amount > player.getMoney()) {
                        System.out.println("Invalid amount! Enter a positive number within your available money.");
                        continue;
                    }
                } else {
                    System.out.println("Invalid amount! Please enter a valid number.");
                    scanner.nextLine(); // Consume invalid input
                    continue;
                }
            }

            // Validate the action
            switch (action) {
                case "bet":
                    if (board.placeBet(amount)) {
                        validAction = true;
                    }
                    break;
                case "raise":
                    if (board.raise(amount)) {
                        validAction = true;
                    }
                    break;
                case "check":
                    if (board.check()) {
                        validAction = true;
                    }
                    break;
                case "fold":
                    board.fold();
                    validAction = true;
                    break;
                default:
                    System.out.println("Invalid action! Choose bet, raise, check, fold, or exchange.");
            }
        }

        // Call a method to proceed to the next player's turn
        board.nextPlayer();
    }

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

    public int getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "PokerGame{" +
                "gameId=" + gameId +
                ", players=" + players +
                '}';
    }
}
