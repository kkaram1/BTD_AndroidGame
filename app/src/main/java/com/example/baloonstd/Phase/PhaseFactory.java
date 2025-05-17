package com.example.baloonstd.Phase;

import android.content.Context;
import android.graphics.Point;

import com.example.baloonstd.Balloon.Balloon;
import com.example.baloonstd.Balloon.BalloonEnemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhaseFactory {
    private Context context;

    public PhaseFactory(Context context) {
        this.context = context;
    }

    public Phase createPhase(int phaseNumber) {
        Map<Balloon, Integer> balloonSetup = new HashMap<>();

        switch (phaseNumber) {
            case 1:
                balloonSetup.put(Balloon.RED, 5);
                balloonSetup.put(Balloon.BLUE, 2);
                break;
            case 2:
                balloonSetup.put(Balloon.RED, 25);
                break;
            case 3:
                balloonSetup.put(Balloon.RED, 10);
                balloonSetup.put(Balloon.BLUE, 30);
                balloonSetup.put(Balloon.ZEPPLIN, 1);
                break;
            case 4:
                balloonSetup.put(Balloon.BLUE, 15);
                balloonSetup.put(Balloon.GREEN, 10);
                break;
            case 5:
                balloonSetup.put(Balloon.BLUE, 10);
                balloonSetup.put(Balloon.GREEN, 20);
                break;
            case 6:
                balloonSetup.put(Balloon.BLUE, 15);
                balloonSetup.put(Balloon.GREEN, 30);
                break;
            case 7:
                balloonSetup.put(Balloon.GREEN, 50);
                balloonSetup.put(Balloon.ZEPPLIN, 1);
                break;
            case 8:
                balloonSetup.put(Balloon.ZEPPLIN, 3);
                break;
            // Add more phases easily here
            default:
                // Fallback phase (if phase number too high)
                balloonSetup.put(Balloon.RED, 50);
                break;
        }

        return new Phase(phaseNumber, createBalloons(balloonSetup));
    }

    private ArrayList<BalloonEnemy> createBalloons(Map<Balloon, Integer> balloonSetup) {
        ArrayList<BalloonEnemy> balloonList = new ArrayList<>();

        for (Map.Entry<Balloon, Integer> entry : balloonSetup.entrySet()) {
            Balloon balloonType = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                balloonList.add(new BalloonEnemy(
                        context,
                        balloonType,
                        new Point(0,0)
                ));
            }
        }



        return balloonList;
    }
}