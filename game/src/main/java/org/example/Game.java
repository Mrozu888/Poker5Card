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

        boolean gameRunning = true;

        while (gameRunning) {
            playRound(board);
            gameRunning = checkGameEndConditions();
        }

        System.out.println("Game Over!");
    }

    private void setupPlayers() {
        players.add(new Player(0, "Kamil", 100));
        players.add(new Player(1, "Piotrek", 120));
        players.add(new Player(2, "Olek", 80));
        players.add(new Player(3, "Dominik", 1000));
    }

    private void playRound(Board board) {
        System.out.println("\nNew Round Begins!");
        boolean roundOver = false;

        while (!roundOver) {
            for (Player player : players) {
                if (!player.hasFolded()) {
                    handlePlayerAction(board, player);

                    if (board.isBettingEqual() || board.getActivePlayerCount() == 1) {
                        roundOver = true;
                        break;
                    }
                }
            }
        }

        board.endRound();
    }


    private void handlePlayerAction(Board board, Player player) {
        System.out.println("\n" + player.getName() + "'s turn!");
        System.out.println("Current pot: " + board.getPot());
        System.out.println("Current bet to match: " + board.getCurrentBet());
        System.out.println("Your money: " + player.getMoney());

        Scanner scanner = new Scanner(System.in);
        String action = null;
        long amount = 0;

        // Use a mutable container to track timeout status
        final boolean[] timeoutOccurred = {false};

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeoutOccurred[0] = true; // Update the array value
                System.out.println("\nTime's up! " + player.getName() + " has been skipped.");
                board.playerMove("fold", 0); // Automatically fold on timeout
            }
        };

        // Start the timer for 2 minutes (120,000 milliseconds)
        timer.schedule(task, 120_000);

        boolean validAction = false;

        // Loop to validate player input
        while (!validAction && !timeoutOccurred[0]) {
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
                    if (amount > 0) {
                        board.playerMove("bet", amount);
                        validAction = true;
                    } else {
                        System.out.println("Bet amount must be greater than 0.");
                    }
                    break;
                case "raise":
                    if (amount > 0) {
                        board.playerMove("raise", amount);
                        validAction = true;
                    } else {
                        System.out.println("Raise amount must be greater than 0.");
                    }
                    break;
                case "check":
                    board.playerMove("check", 0);
                    validAction = true;
                    break;
                case "fold":
                    board.playerMove("fold", 0);
                    validAction = true;
                    break;
                default:
                    System.out.println("Invalid action! Choose bet, raise, check, or fold.");
            }
        }

        // Cancel the timer if the player finishes their turn
        timer.cancel();

        if (timeoutOccurred[0]) {
            System.out.println(player.getName() + " automatically folded due to timeout.");
        }
    }



    private boolean checkGameEndConditions() {
        long playersWithChips = players.stream()
                .filter(player -> player.getMoney() > 0 && !player.hasFolded())
                .count();

        if (playersWithChips <= 1) {
            System.out.println("Game ending as there is only one player left with chips.");
            return false;
        }

        return true;
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
