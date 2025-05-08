package com.example.baloonstd;

public class PlayerManager {
    private static final PlayerManager instance = new PlayerManager();
    private Player currentPlayer;

    private PlayerManager() {}

    public static PlayerManager getInstance() {
        return instance;
    }

    public void setPlayer(Player player) {
        this.currentPlayer = player;
    }

    public Player getPlayer() {
        return currentPlayer;
    }
}
