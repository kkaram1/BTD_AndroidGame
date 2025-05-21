package com.example.baloonstd.Player;

public class Player {
    private  String username;
    private int balloonsPopped;
    private int towersPlaced;
    private boolean guest;
    private int gamesPlayed;
    private  int  playerId;
    private int towerUpgraded;
    private int highestRound;
    public Player(String username,int balloonsPopped,int towersPlaced,boolean guest,int gamesPlayed,int playerId,int towerUpgraded,int highestRound) {
        this.username = username;
        this.balloonsPopped = balloonsPopped;
        this.towersPlaced=towersPlaced;
        this.guest =guest;
        this.gamesPlayed = gamesPlayed;
        this.playerId = playerId;
        this.towerUpgraded =towerUpgraded;
        this.highestRound =highestRound;
    }
    public Player (String username,int balloonsPopped,int gamesPlayed)
    {
        this.username = username;
        this.balloonsPopped = balloonsPopped;
        this.gamesPlayed = gamesPlayed;
    }

    public int getTowersPlaced() {return towersPlaced;}
    public void setTowerUpgraded(int towerUpgraded) {this.towerUpgraded = towerUpgraded;}
    public int getPlayerId() {return playerId;}

    public boolean setHighestRound(int highestRound) {
        if(this.highestRound<highestRound)
        {this.highestRound = highestRound;
        return true;}
        return false;
    }

    public void incTowers(int inc){towersPlaced+=inc;}

    public String getUsername() {return username;}

    public boolean isGuest() {return guest;}

    public int getBalloonsPopped() {return balloonsPopped;}

    public int getGamesPlayed() {return gamesPlayed;}

    public void setGamesPlayed(int gamesPlayed) {this.gamesPlayed = gamesPlayed;}


    public void setBalloonsPopped(int balloonsPopped) {this.balloonsPopped = balloonsPopped;}

    public int getTowersUpgraded() {return towerUpgraded;}

    public int getHighestRound() {return highestRound;}
}
