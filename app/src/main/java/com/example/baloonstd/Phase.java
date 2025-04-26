package com.example.baloonstd;

import java.util.ArrayList;

public class Phase {
    private ArrayList<BalloonEnemy> balloons;
    private int phaseNum;
    public Phase(int phaseNum, ArrayList<BalloonEnemy> balloons) {
        this.phaseNum = phaseNum;
        this.balloons = balloons;
    }

    public ArrayList<BalloonEnemy> getBalloons() {
        return balloons;
    }

    public int getPhaseNum() {
        return phaseNum;
    }
}
