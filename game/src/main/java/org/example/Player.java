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
    private boolean folded;

    public Player(long id, String name, long money) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.state = State.WAITING;
        this.folded = false;
    }

    public void drawCards(List<Card> cards) {
        this.hand = new Hand(cards);
    }

    public void exchangeCards(int[] indexes, List<Card> newCards) {
        this.hand.exchangeCards(indexes, newCards);
    }

    public Hand getHand() {
        return hand;
    }

    public void placeBet(long amount) {
        if (amount > money) {
            System.out.println("You don't have enough money!");
        }
        else{
            money -= amount;
            this.bet = amount;
        }
    }

    public void addWinnings(long amount){
        this.money += amount;
    }

    public long getBet() {
        return bet;
    }

    public void fold() {
        this.folded = true;
    }

    public boolean hasFolded() {
        return this.folded;
    }

    public long getMoney() {
        return money;
    }
    public String getName() {
        return name;
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
