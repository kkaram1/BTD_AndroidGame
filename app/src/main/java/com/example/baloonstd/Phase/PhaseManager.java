package com.example.baloonstd.Phase;

import android.content.Context;

import com.example.baloonstd.Difficulty;

public class PhaseManager {
    private int currentPhase = 1;
    private Phase currentPhaseData;
    private PhaseFactory phaseFactory;

    public PhaseManager(Context context, Difficulty difficulty) {
        phaseFactory = new PhaseFactory(context, difficulty);
        loadPhase(currentPhase);
    }

    private void loadPhase(int phaseNumber) {
        currentPhaseData = phaseFactory.createPhase(phaseNumber);
    }

    public Phase getCurrentPhase() {
        return currentPhaseData;
    }

    public boolean hasNextPhase() {
        return currentPhase < 50;
    }

    public Phase moveToNextPhase() {
        if (hasNextPhase()) {
            currentPhase++;
            loadPhase(currentPhase);
            return currentPhaseData;
        }
        return null;
    }

    public int getCurrentPhaseNum() {
        return currentPhase;
    }
}
