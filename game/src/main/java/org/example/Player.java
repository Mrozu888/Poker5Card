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
    private boolean checking;
    private boolean exchanged;

    public Player(long id, String name, long money) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.state = State.WAITING;
        this.folded = false;
        this.bet = 0;
        this.exchanged = false;
        this.checking = false;
    }

    public void drawCards(List<Card> cards) {
        this.hand = new Hand(cards);
    }

    public void exchangeCards(int[] indexes, List<Card> newCards) {
        this.hand.exchangeCards(indexes, newCards);
        this.setExchanged(true);
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

    public void setChecking(boolean checking) {
        this.checking = checking;
    }
    public boolean isChecking() {
        return this.checking;
    }

    public boolean isExchanged() {
        return exchanged;
    }
    public void setExchanged(boolean exchanged) {
        this.exchanged = exchanged;
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
