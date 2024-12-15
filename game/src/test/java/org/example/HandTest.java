package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    @Test
    void testHandInitialization() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.TEN, Suit.HEARTS)
        );
        Hand hand = new Hand(cards);

        assertEquals(5, hand.getCards().size());
        assertEquals(cards, hand.getCards());
    }

    @Test
    void testInvalidHandSize() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS)
        );

        assertThrows(IllegalArgumentException.class, () -> new Hand(cards));
    }

    @Test
    void testExchangeCards() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.SIX, Suit.HEARTS)
        );
        Hand hand = new Hand(cards);

        List<Card> newCards = Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.KING, Suit.DIAMONDS)
        );
        hand.exchangeCards(new int[]{0, 1}, newCards);

        assertEquals(newCards.get(0), hand.getCards().get(0));
        assertEquals(newCards.get(1), hand.getCards().get(1));
    }

    @Test
    void testIsStraightFlush() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.SIX, Suit.HEARTS)
        );
        Hand hand = new Hand(cards);

        String handType = hand.evaluateHand();
        assertEquals("Straight Flush (Relevant card: SIX)", handType);
        assertEquals("Straight Flush", hand.getHandType());
    }

    @Test
    void testIsStraight() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.SIX, Suit.HEARTS)
        );
        Hand hand = new Hand(cards);

        String handType = hand.evaluateHand();
        assertEquals("Straight (Relevant card: SIX)", handType);
        assertEquals("Straight", hand.getHandType());
    }

    @Test
    void testFullHouse() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.TWO, Suit.DIAMONDS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.SPADES),
                new Card(Rank.THREE, Suit.HEARTS)
        );
        Hand hand = new Hand(cards);

        String handType = hand.evaluateHand();
        assertEquals("Full House (Relevant card: TWO)", handType);
        assertEquals("Full House", hand.getHandType());
    }

    @Test
    void testHighCard() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.FOUR, Suit.DIAMONDS),
                new Card(Rank.SIX, Suit.CLUBS),
                new Card(Rank.EIGHT, Suit.SPADES),
                new Card(Rank.TEN, Suit.HEARTS)
        );
        Hand hand = new Hand(cards);

        String handType = hand.evaluateHand();
        assertEquals("High Card (Relevant card: TEN)", handType);
        assertEquals("High Card", hand.getHandType());
    }

    @Test
    void testHandComparison() {
        Hand hand1 = new Hand(Arrays.asList(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.TEN, Suit.HEARTS)
        )); // Straight Flush

        Hand hand2 = new Hand(Arrays.asList(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.SIX, Suit.HEARTS)
        )); // Straight Flush with lower rank

        assertTrue(hand1.compareTo(hand2) > 0); // hand1 is better
    }
}
