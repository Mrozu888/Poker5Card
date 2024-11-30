package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
    private final List<Card> cards;
    private int rankValue;
    private int handValue;
    private String handType;

    public Hand(List<Card> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A hand must contain exactly 5 cards.");
        }
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void exchangeCards(int[] indexes, List<Card> newCards) {
        if (newCards == null || newCards.isEmpty()) {
            System.out.println("No cards to exchange.");
            return;
        }

        int n = indexes.length;
        for (int i = 0; i < n; i++) {
            this.cards.set(indexes[i], newCards.get(i));
        }

        System.out.println("Cards exchanged successfully.");
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
        boolean isAceLow = ranks.get(0) == 0 && ranks.get(1) == 1 && ranks.get(2) == 2 && ranks.get(3) == 3 && ranks.get(4) == 12;
        if (isAceLow) return true;
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

    // Get a ranking value for the hand (higher is better)
    private int getHandRank() {
        Map<Integer, Long> frequencies = rankFrequency();
        Collection<Long> counts = frequencies.values();

        boolean flush = isFlush();
        boolean straight = isStraight();

        if (flush && straight) return 8; // Straight Flush
        if (counts.contains(4L)) return 7; // Four of a Kind
        if (counts.contains(3L) && counts.contains(2L)) return 6; // Full House
        if (flush) return 5; // Flush
        if (straight) return 4; // Straight
        if (counts.contains(3L)) return 3; // Three of a Kind
        if (Collections.frequency(counts, 2L) == 2) return 2; // Two Pair
        if (counts.contains(2L)) return 1; // One Pair

        return 0; // High Card
    }

    // Get the high card or tie-breaking ranks
    private List<Integer> getTieBreakingRanks() {
        Map<Integer, Long> frequencies = rankFrequency();
        return frequencies.entrySet().stream()
                .sorted((e1, e2) -> {
                    int frequencyComparison = Long.compare(e2.getValue(), e1.getValue());
                    if (frequencyComparison != 0) return frequencyComparison;
                    return Integer.compare(e2.getKey(), e1.getKey()); // Break ties by rank
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Compare this hand to another hand
    public int compareTo(Hand other) {
        // Compare hand ranks first
        int thisRank = this.getHandRank();
        int otherRank = other.getHandRank();
        if (thisRank != otherRank) {
            return Integer.compare(thisRank, otherRank);
        }

        // If ranks are the same, compare tie-breaking ranks
        List<Integer> thisTieBreakers = this.getTieBreakingRanks();
        List<Integer> otherTieBreakers = other.getTieBreakingRanks();

        for (int i = 0; i < Math.min(thisTieBreakers.size(), otherTieBreakers.size()); i++) {
            int comparison = thisTieBreakers.get(i).compareTo(otherTieBreakers.get(i));
            if (comparison != 0) {
                return comparison;
            }
        }

        return 0; // Hands are completely equal
    }

    public String evaluateHand() {
        Map<Integer, Long> frequencies = rankFrequency();
        String handType;
        int relevantRank = -1;
        this.handValue = this.getHandRank();
        switch (this.handValue) {
            case 8: // Straight Flush
            case 4: // Straight
                relevantRank = getSortedRanks().get(getSortedRanks().size() - 1); // Highest rank in the straight
                handType = getHandRank() == 8 ? "Straight Flush" : "Straight";
                break;
            case 7: // Four of a Kind
                relevantRank = getRankWithFrequency(4, frequencies); // Rank with frequency 4
                handType = "Four of a Kind";
                break;
            case 6: // Full House
                relevantRank = getRankWithFrequency(3, frequencies); // Rank with frequency 3
                handType = "Full House";
                break;
            case 5: // Flush
                relevantRank = getSortedRanks().get(getSortedRanks().size() - 1); // Highest card in flush
                handType = "Flush";
                break;
            case 3: // Three of a Kind
                relevantRank = getRankWithFrequency(3, frequencies); // Rank with frequency 3
                handType = "Three of a Kind";
                break;
            case 2: // Two Pair
                List<Integer> pairs = getRanksWithFrequency(2, frequencies);
                pairs.sort(Collections.reverseOrder()); // Sort pairs by rank
                relevantRank = pairs.get(0); // Highest pair
                handType = "Two Pair";
                break;
            case 1: // One Pair
                relevantRank = getRankWithFrequency(2, frequencies); // Rank with frequency 2
                handType = "One Pair";
                break;
            default: // High Card
                relevantRank = getSortedRanks().get(getSortedRanks().size() - 1); // Highest card
                handType = "High Card";
                break;
        }

        // Append the relevant rank to the hand type
        this.handType = handType;
        this.rankValue = relevantRank;

        return handType + " (Relevant card: " + Rank.values()[relevantRank] + ")";
    }

    // Helper method to find the rank with a specific frequency
    private int getRankWithFrequency(int frequency, Map<Integer, Long> frequencies) {
        return frequencies.entrySet().stream()
                .filter(entry -> entry.getValue() == frequency)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No rank with frequency " + frequency));
    }

    // Helper method to find all ranks with a specific frequency
    private List<Integer> getRanksWithFrequency(int frequency, Map<Integer, Long> frequencies) {
        return frequencies.entrySet().stream()
                .filter(entry -> entry.getValue() == frequency)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public int[] getEvaluatedValues(){
        return new int[]{this.handValue, this.rankValue};
    }

    public String getHandType() {
        return handType;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
