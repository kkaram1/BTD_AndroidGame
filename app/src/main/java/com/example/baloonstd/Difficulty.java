package com.example.baloonstd;

public enum Difficulty {
    EASY(6),
    MEDIUM(5),
    HARD(4);

    private final int rewardPerLayer;

    Difficulty(int rewardPerLayer) {
        this.rewardPerLayer = rewardPerLayer;
    }
    public int getRewardPerLayer() {
        return rewardPerLayer;
    }
}
