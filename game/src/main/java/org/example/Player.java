package org.example;

import java.util.ArrayList;
import java.util.List;

enum State{
    FOLD, CHECK, BET, EXCHANGE, WAITING
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
        this.bet = 0;
    }

    public void drawCards(List<Card> cards) {
        this.hand = new Hand(cards);
    }

    public void exchangeCards(int[] indexes, List<Card> newCards) {
        this.hand.exchangeCards(indexes, newCards);
        this.state = State.EXCHANGE;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean placeBet(long amount) {
        long addedBet = amount - this.bet;
        if (addedBet > money) {
            System.out.println("You don't have enough money!");
            return false;
        }
        else{
            this.money -= addedBet;
            this.bet = amount;
            return true;
        }
    }

    public void addWinnings(long amount){
        this.money += amount;
    }

    public long getBet() {
        return bet;
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

    public void setState(State state) {
        this.state = state;
    }
    public State getState() {
        return this.state;
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
