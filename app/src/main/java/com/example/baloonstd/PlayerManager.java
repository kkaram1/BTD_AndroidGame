package com.example.baloonstd;

public class PlayerManager {
    private static PlayerManager instance;
    private Player player;
    private PlayerManager() {}

    // Singleton method to get the instance
    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }
    public void setPlayer(Player player) {this.player = player;}
    public Player getPlayer() {return player;}
}
