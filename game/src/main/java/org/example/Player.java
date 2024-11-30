package org.example;

import java.util.ArrayList;
import java.util.List;

enum State{
    FOLD, CALL, RAISE, ALLIN, WAITING
}

public class Player {
    private List<Card> cards;
    private long money;
    private long bet;
    private String name;
    private State state;
    private long id;

    public Player(long id, String name, long money) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.state = State.WAITING;
    }

    public void drawCards(List<Card> cards) {
        this.cards = cards;
    }

    public void exchangeCards(int[] indexes, List<Card> newCards) {
        int n = indexes.length;
        for (int i = 0; i < n; i++) {
            this.cards.set(indexes[i], newCards.get(i));
        }

        if (newCards == null || newCards.isEmpty()) {
            System.out.println("No cards to exchange.");
            return;
        }

        // Check if the player has cards to exchange
        if (this.cards == null || this.cards.isEmpty()) {
            System.out.println("Player has no cards to exchange.");
            return;
        }

        System.out.println("Cards exchanged successfully.");
    }


    public long raiseBet(long amount) {
        if (amount > this.money) {
            System.out.println("You don't have enough money to raise the bet");
        }
        else{
            this.money -= amount;
            this.bet += amount;
        }
        return this.bet;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bet=" + bet +
                ", state=" + state +
                ", cards=" + cards +
                '}';
    }
}
