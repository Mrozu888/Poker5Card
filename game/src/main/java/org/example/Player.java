package org.example;

import java.util.List;

public class Player {
    private List<Card> cards;
    private long money;
    private long bet;
    private String name;

    public Player(String name, long money) {
        this.name = name;
        this.money = money;
    }
    public void drawCards(List<Card> cards) {
        this.cards = cards;
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
}
