package com.example.baloonstd.Phase;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.baloonstd.Balloon;
import com.example.baloonstd.BalloonEnemy;

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
                balloonSetup.put(Balloon.RED, 10);
                break;
            case 2:
                balloonSetup.put(Balloon.RED, 20);
                break;
            case 3:
                balloonSetup.put(Balloon.RED, 10);
                balloonSetup.put(Balloon.BLUE, 5);
                break;
            case 4:
                balloonSetup.put(Balloon.BLUE, 15);
                balloonSetup.put(Balloon.GREEN, 10);
                break;
            case 5:
                balloonSetup.put(Balloon.GREEN, 20);
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

            // Decode and scale ONCE per balloon type
            Bitmap original = BitmapFactory.decodeResource(context.getResources(), balloonType.getResourceId());
            Bitmap scaled = Bitmap.createScaledBitmap(original, 100, 100, true); // Your desired size

            for (int i = 0; i < count; i++) {
                balloonList.add(new BalloonEnemy(
                        scaled,
                        balloonType.getSpeed(),
                        balloonType.getLayer(),
                        new Point(0, 0)
                ));
            }
        }



        return balloonList;
    }
}