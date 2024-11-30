package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
    private final List<Card> cards;

    public Hand(List<Card> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards.");
        }
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    // Get the rank values as integers for easier comparison
    private List<Integer> getSortedRanks() {
        return cards.stream()
                .map(card -> card.getRank().ordinal())
                .sorted()
                .collect(Collectors.toList());
    }

    // Check for a flush
    private boolean isFlush() {
        Suit suit = cards.get(0).getSuit();
        return cards.stream().allMatch(card -> card.getSuit() == suit);
    }

    // Check for a straight
    private boolean isStraight() {
        List<Integer> ranks = getSortedRanks();
        // Check for Ace-low straight (e.g., A-2-3-4-5)
        boolean isAceLow = ranks.get(0) == 0 && ranks.get(1) == 1 && ranks.get(2) == 2 && ranks.get(3) == 3 && ranks.get(4) == 12;
        if (isAceLow) return true;
        // Check normal sequential straight
        for (int i = 0; i < ranks.size() - 1; i++) {
            if (ranks.get(i) + 1 != ranks.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    // Count the occurrences of each rank
    private Map<Integer, Long> rankFrequency() {
        return cards.stream()
                .collect(Collectors.groupingBy(card -> card.getRank().ordinal(), Collectors.counting()));
    }

    // Determine the rank of the hand
    public String evaluateHand() {
        Map<Integer, Long> frequencies = rankFrequency();
        Collection<Long> counts = frequencies.values();

        boolean flush = isFlush();
        boolean straight = isStraight();

        if (flush && straight) return "Straight Flush";
        if (counts.contains(4L)) return "Four of a Kind";
        if (counts.contains(3L) && counts.contains(2L)) return "Full House";
        if (flush) return "Flush";
        if (straight) return "Straight";
        if (counts.contains(3L)) return "Three of a Kind";
        if (Collections.frequency(counts, 2L) == 2) return "Two Pair";
        if (counts.contains(2L)) return "One Pair";

        return "High Card";
    }
}
