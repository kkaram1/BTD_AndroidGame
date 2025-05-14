package com.example.baloonstd;

public class Player {
    private  String username;
    private int balloonsPopped;
    private int towersPlaced;

    public Player(String username,int balloonsPopped,int towersPlaced) {
        this.username = username;
        this.balloonsPopped = balloonsPopped;
        this.towersPlaced=towersPlaced;
    }

    public int getTowersPlaced() {
        return towersPlaced;
    }
    public void incTowers(int inc){towersPlaced+=inc;}

    public String getUsername() {return username;}

    public int getBalloonsPopped() {
        return balloonsPopped;
    }

    public void incrementBalloonsPopped(int amount) {
        this.balloonsPopped += amount;
    }

    public void setBalloonsPopped(int balloonsPopped) {this.balloonsPopped = balloonsPopped;}
}
