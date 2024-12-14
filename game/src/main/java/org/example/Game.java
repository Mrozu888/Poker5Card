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
                timeoutOccurred[0] = true; // Mark timeout
                System.out.println("\nTime's up! " + player.getName() + " has been skipped.");
                board.playerMove("fold", 0, null); // Automatically fold on timeout
            }
        };

        // Start the timer for 2 minutes (120,000 milliseconds)
        timer.schedule(task, 120_000);

        boolean validAction = false;

        // Loop to validate player input
        while (!validAction && !timeoutOccurred[0]) {
            System.out.println("Choose action: (bet, raise, check, fold, exchange)");
            action = scanner.next().toLowerCase();

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
            } else if (action.equals("exchange")) {
                // Handle exchange action
                System.out.println("Enter card indices to exchange (comma-separated, e.g., 0,2,3), or press Enter to skip:");
                scanner.nextLine(); // Consume previous newline
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println(player.getName() + " chose not to exchange any cards.");
                    continue;
                }

                // Parse indices
                String[] parts = input.split(",");
                int[] cardIndexes = new int[parts.length];
                boolean validInput = true;

                for (int i = 0; i < parts.length; i++) {
                    try {
                        cardIndexes[i] = Integer.parseInt(parts[i].trim());
                        if (cardIndexes[i] < 0 || cardIndexes[i] >= board.getCardAmount()) {
                            System.out.println("Invalid card index: " + cardIndexes[i]);
                            validInput = false;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Please enter valid card indices.");
                        validInput = false;
                        break;
                    }
                }

                if (!validInput) {
                    System.out.println("Invalid exchange attempt. Try again.");
                    continue;
                }

                // Perform the exchange action
                board.playerMove("exchange", 0, cardIndexes);
                System.out.println(player.getName() + "'s new hand: " + player.getHand());
                validAction = true;
                continue;
            }

            // Validate the action
            switch (action) {
                case "bet":
                    if (amount > 0) {
                        board.placeBet(amount);
                        validAction = true;
                    } else {
                        System.out.println("Bet amount must be greater than 0.");
                    }
                    break;
                case "raise":
                    if (amount > 0) {
                        board.raise(amount);
                        validAction = true;
                    } else {
                        System.out.println("Raise amount must be greater than 0.");
                    }
                    break;
                case "check":
                    board.check();
                    validAction = true;
                    break;
                case "fold":
                    board.fold();
                    validAction = true;
                    break;
                default:
                    System.out.println("Invalid action! Choose bet, raise, check, fold, or exchange.");
            }
        }

        // Cancel the timer if the player finishes their turn
        timer.cancel();

        // If timeout occurred, advance to the next player
        if (timeoutOccurred[0]) {
            System.out.println(player.getName() + " automatically folded due to timeout.");
        }

        // Call a method to proceed to the next player's turn
        board.nextPlayer();
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
