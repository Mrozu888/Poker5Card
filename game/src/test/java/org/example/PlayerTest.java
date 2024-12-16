package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    private List<Card> initialCards;

    @BeforeEach
    void setUp() {
        player = new Player(1, "TestPlayer", 1000);
        initialCards = new ArrayList<>();
        initialCards.add(new Card(Suit.HEARTS, Rank.ACE));
        initialCards.add(new Card(Suit.DIAMONDS, Rank.KING));
        initialCards.add(new Card(Suit.CLUBS, Rank.QUEEN));
        initialCards.add(new Card(Suit.SPADES, Rank.JACK));
        initialCards.add(new Card(Suit.HEARTS, Rank.TEN));
        player.drawCards(initialCards);
    }

    @Test
    void testDrawCards() {
        assertNotNull(player.getHand());
        assertEquals(5, player.getHand().size());
        assertEquals(initialCards, player.getHand().getCards());
    }

    @Test
    void testExchangeCards() {
        // Setup new cards for exchange
        List<Card> newCards = new ArrayList<>();
        newCards.add(new Card(Suit.HEARTS, Rank.TWO));
        newCards.add(new Card(Suit.DIAMONDS, Rank.THREE));

        // Exchange first two cards
        int[] indexes = {0, 1};
        player.exchangeCards(indexes, newCards);

        assertEquals(newCards.get(0), player.getHand().getCards().get(0));
        assertEquals(newCards.get(1), player.getHand().getCards().get(1));
        assertEquals(State.EXCHANGE, player.getState());
    }

    @Test
    void testPlaceBetValid() {
        assertTrue(player.placeBet(500));
        assertEquals(500, player.getBet());
        assertEquals(500, player.getMoney());
    }

    @Test
    void testPlaceBetInvalid() {
        assertFalse(player.placeBet(1500)); // More than available money
        assertEquals(0, player.getBet());
        assertEquals(1000, player.getMoney()); // No money should be deducted
    }

    @Test
    void testAddWinnings() {
        player.addWinnings(500);
        assertEquals(1500, player.getMoney());
    }

    @Test
    void testStateManagement() {
        player.setState(State.FOLD);
        assertEquals(State.FOLD, player.getState());

        player.setState(State.CHECK);
        assertEquals(State.CHECK, player.getState());
    }

    @Test
    void testToString() {
        String expected = "Player{" +
                "id=1, name='TestPlayer', bet=0, state=WAITING, cards=" + player.getHand() +
                '}';
        assertEquals(expected, player.toString());
    }
}
