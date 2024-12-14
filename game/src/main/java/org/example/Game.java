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
        setup();
    }

    public void setup(){
        board = new Board(players);
    }

    public void start() {
        setupPlayers();

        board.start();

        System.out.println("Starting Poker Game!");
        System.out.println(board);

        while (!isRoundOver()){
            handlePlayerBet(board);
        }


    }

    private void setupPlayers() {
        players.add(new Player(0, "Kamil", 100));
        players.add(new Player(1, "Piotrek", 120));
        players.add(new Player(2, "Olek", 80));
        players.add(new Player(3, "Dominik", 1000));
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
        Player player = board.getFirstPlayer();

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
            System.out.println("Choose cards: (bet, raise, check, fold)");
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

    private void exchangeRound(){

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

    @Override
    public String toString() {
        return "PokerGame{" +
                "gameId=" + gameId +
                ", players=" + players +
                '}';
    }
}
