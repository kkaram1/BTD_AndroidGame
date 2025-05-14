package com.example.baloonstd;

public class Player {
    private String username;
    private int balloonsPopped;

    public Player(String username) {
        this.username = username;
        this.balloonsPopped = 0;
    }

    public String getUsername() {return username;}

    public int getBalloonsPopped() {
        return balloonsPopped;
    }

    public void incrementBalloonsPopped(int amount) {
        this.balloonsPopped += amount;
    }

    public void setBalloonsPopped(int balloonsPopped) {this.balloonsPopped = balloonsPopped;}
}
