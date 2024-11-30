package org.example;

import java.util.Objects;

enum Rank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

// Enum representing the suit of the card
enum Suit {
    HEARTS, DIAMONDS, CLUBS, SPADES
}

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        return formatCard();
    }

    // Helper method to format the card with suit symbols and color
    private String formatCard() {
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String BLACK = "\u001B[30m";

        // Map suits to symbols and colors
        String suitSymbol;
        String color;
        switch (suit) {
            case HEARTS:
                suitSymbol = "♥";
                color = RED;
                break;
            case DIAMONDS:
                suitSymbol = "♦";
                color = RED;
                break;
            case CLUBS:
                suitSymbol = "♣";
                color = BLACK;
                break;
            case SPADES:
                suitSymbol = "♠";
                color = BLACK;
                break;
            default:
                suitSymbol = "?";
                color = RESET;
        }

        // Map ranks to human-readable strings
        String rankString;
        switch (rank) {
            case TWO: rankString = "2"; break;
            case THREE: rankString = "3"; break;
            case FOUR: rankString = "4"; break;
            case FIVE: rankString = "5"; break;
            case SIX: rankString = "6"; break;
            case SEVEN: rankString = "7"; break;
            case EIGHT: rankString = "8"; break;
            case NINE: rankString = "9"; break;
            case TEN: rankString = "10"; break;
            case JACK: rankString = "J"; break;
            case QUEEN: rankString = "Q"; break;
            case KING: rankString = "K"; break;
            case ACE: rankString = "A"; break;
            default: rankString = "?";
        }

        return color + rankString + suitSymbol + RESET;
    }

    public static void main(String[] args) {
        // Example of printing a deck of cards
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(rank, suit);
                System.out.print(card + " ");
            }
            System.out.println();
        }
    }
}
