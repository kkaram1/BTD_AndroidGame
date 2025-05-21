package com.example.baloonstd;

public enum Difficulty {
    EASY(8),
    MEDIUM(6),
    HARD(6);

    private final int rewardPerLayer;

    Difficulty(int rewardPerLayer) {
        this.rewardPerLayer = rewardPerLayer;
    }
    public int getRewardPerLayer() {
        return rewardPerLayer;
    }
}
