package org.example;

import java.util.ArrayList;
import java.util.List;

enum State{
    FOLD, CALL, RAISE, ALLIN, WAITING
}

public class Player {
    private Hand hand;
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
        this.hand = new Hand(cards);
    }

    public void exchangeCards(int[] indexes, List<Card> newCards) {
        hand.exchangeCards(indexes, newCards);
    }

    public Hand getHand() {
        return hand;
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

    public long getMoney() {
        return money;
    }

    public int[] getHandValues(){
        return this.hand.getEvaluatedValues();
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bet=" + bet +
                ", state=" + state +
                ", cards=" + hand +
                '}';
    }
}
