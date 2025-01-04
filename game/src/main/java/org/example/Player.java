package org.example;

import lombok.Data;

import java.util.List;


@Data
public class Player {
    private Hand hand;
    private long money;
    private long bet;
    private String name;
    private State state;
    private int id;

    public Player(int id, String name, long money) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.state = State.LOBBY;
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

    public Boolean placeBet(long amount) {
        long addedBet = amount - this.bet;
        if (addedBet > money) {
            return false;
        }
        else{
            this.money -= addedBet;
            this.bet = amount;
            state = State.BET;
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

    public int getId() {
        return id;
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
