package com.example.baloonstd.Player;

public class Player {
    private  String username;
    private int balloonsPopped;
    private int towersPlaced;
    private boolean guest;
    private int gamesPlayed;
    private  int  playerId;

    public Player(String username,int balloonsPopped,int towersPlaced,boolean guest,int gamesPlayed,int playerId) {
        this.username = username;
        this.balloonsPopped = balloonsPopped;
        this.towersPlaced=towersPlaced;
        this.guest =guest;
        this.gamesPlayed = gamesPlayed;
        this.playerId = playerId;
    }
    public Player (String username,int balloonsPopped,int gamesPlayed)
    {
        this.username = username;
        this.balloonsPopped = balloonsPopped;
        this.gamesPlayed = gamesPlayed;
    }

    public int getTowersPlaced() {
        return towersPlaced;
    }

    public int getPlayerId() {return playerId;}

    public void incTowers(int inc){towersPlaced+=inc;}

    public String getUsername() {return username;}

    public boolean isGuest() {return guest;}

    public int getBalloonsPopped() {
        return balloonsPopped;
    }

    public int getGamesPlayed() {return gamesPlayed;}

    public void setGamesPlayed(int gamesPlayed) {this.gamesPlayed = gamesPlayed;}

    public void incrementBalloonsPopped(int amount) {
        this.balloonsPopped += amount;
    }

    public void setBalloonsPopped(int balloonsPopped) {this.balloonsPopped = balloonsPopped;}

    public int getTowersUpgraded() {return balloonsPopped;
    }

    public int getHighestRound() {return balloonsPopped;
    }

    public int getGoldEarned() {return balloonsPopped;
    }

    public int getCurrentGold() {return balloonsPopped;
    }

    public boolean hasFlawlessRound() {return true;
    }

    public int getMinLifeLeftInARound() {return balloonsPopped;
    }
}
