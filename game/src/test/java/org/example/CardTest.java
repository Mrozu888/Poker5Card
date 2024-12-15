package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCardInitialization() {
        Card card = new Card(Rank.ACE, Suit.HEARTS);
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(Suit.HEARTS, card.getSuit());
    }

    @Test
    void testCardEquality() {
        Card card1 = new Card(Rank.KING, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);

        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }

    @Test
    void testCardHashCode() {
        Card card1 = new Card(Rank.TEN, Suit.SPADES);
        Card card2 = new Card(Rank.TEN, Suit.SPADES);

        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testCardToString() {
        Card card = new Card(Rank.JACK, Suit.DIAMONDS);

        // Ensure the string representation includes the correct rank and suit
        String cardString = card.toString();
        assertTrue(cardString.contains("J"));
        assertTrue(cardString.contains("♦"));
    }

    @Test
    void testDifferentCardToString() {
        Card card = new Card(Rank.SEVEN, Suit.HEARTS);

        // Test for the correct formatting with suit and rank
        String cardString = card.toString();
        assertTrue(cardString.contains("7"));
        assertTrue(cardString.contains("♥"));
    }

    @Test
    void testCardColorFormatting() {
        Card redCard = new Card(Rank.NINE, Suit.DIAMONDS);
        Card blackCard = new Card(Rank.THREE, Suit.SPADES);

        // Check if the formatting includes color codes for red and black suits
        String redCardString = redCard.toString();
        String blackCardString = blackCard.toString();

        assertTrue(redCardString.contains("\u001B[31m")); // RED
        assertTrue(blackCardString.contains("\u001B[30m")); // BLACK
    }
}
