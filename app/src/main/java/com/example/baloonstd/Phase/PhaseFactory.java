package com.example.baloonstd.Phase;

import android.content.Context;
import android.graphics.Point;

import com.example.baloonstd.Balloon.Balloon;
import com.example.baloonstd.Balloon.BalloonEnemy;
import com.example.baloonstd.Difficulty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhaseFactory {
    private final Context context;
    private final Difficulty difficulty;

    public PhaseFactory(Context context, Difficulty difficulty) {
        this.context = context;
        this.difficulty = difficulty;
    }

    public Phase createPhase(int phaseNumber) {
        Map<Balloon, Integer> balloonSetup = new HashMap<>();

        switch (difficulty) {
            case EASY:   setupEasy(phaseNumber, balloonSetup);   break;
            case MEDIUM: setupMedium(phaseNumber, balloonSetup); break;
            case HARD:   setupHard(phaseNumber, balloonSetup);   break;
        }

        return new Phase(phaseNumber, createBalloons(balloonSetup));
    }

    private void setupEasy(int p, Map<Balloon,Integer> m){
        switch (p) {
            case 1:
                m.put(Balloon.RED,    5);
                break;
            case 2:
                m.put(Balloon.RED,    8);
                break;
            case 3:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,    2);
                break;
            case 4:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,    4);
                break;
            case 5:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   4);
                m.put(Balloon.ZEPPLIN,   1);
                break;
            case 6:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLACK,   3);
                m.put(Balloon.ZEPPLINBLACK,   1);
                break;
            case 7:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,   10);
                break;
            case 8:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,  15);
                break;
            case 9:
                m.put(Balloon.RED,    5);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,  25);
                break;
            case 10:
                m.put(Balloon.BLUE,   20);
                m.put(Balloon.GREEN,  20);
                break;
            case 11:
                m.put(Balloon.BLUE,   15);
                m.put(Balloon.BLACK,  3);
                break;
            case 12:
                m.put(Balloon.GREEN,  10);
                m.put(Balloon.BLACK,  6);
                break;
            case 13:
                m.put(Balloon.BLUE,   5);
                m.put(Balloon.GREEN,  5);
                m.put(Balloon.BLACK,  7);
                break;
            case 14:
                m.put(Balloon.ZEPPLIN, 1);
                break;
            case 15:
                m.put(Balloon.BLACK,  4);
                m.put(Balloon.ZEPPLIN, 1);
                break;
            case 16:
                m.put(Balloon.ZEPPLIN, 2);
                break;
            case 17:
                m.put(Balloon.GREEN,  10);
                m.put(Balloon.BLACK,  4);
                m.put(Balloon.ZEPPLIN, 2);
                break;
            case 18:
                m.put(Balloon.ZEPPLIN, 3);
                break;
            case 19:
                m.put(Balloon.RED,   5);
                m.put(Balloon.BLUE,   5);
                m.put(Balloon.GREEN,  5);
                m.put(Balloon.BLACK,  5);
                m.put(Balloon.ZEPPLIN, 3);
                break;
            case 20:
                m.put(Balloon.ZEPPLINBLACK, 2);
                break;
            default: m.put(Balloon.RED, 20);                          break;
        }
    }
    private void setupMedium(int p, Map<Balloon,Integer> m){
        switch(p){
            case 1:
                m.put(Balloon.BLACK,    5);
                break;
            case 2:
                m.put(Balloon.RED,    8);
                break;
            case 3:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,    2);
                break;
            case 4:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,    4);
                break;
            case 5:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   4);
                m.put(Balloon.GREEN,   3);
                break;
            case 6:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,   5);
                break;
            case 7:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,   10);
                break;
            case 8:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,  15);
                break;
            case 9:
                m.put(Balloon.RED,    5);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,  25);
                break;
            case 10:
                m.put(Balloon.BLUE,   20);
                m.put(Balloon.GREEN,  20);
                break;
            case 11:
                m.put(Balloon.BLUE,   15);
                m.put(Balloon.BLACK,  3);
                break;
            case 12:
                m.put(Balloon.GREEN,  10);
                m.put(Balloon.BLACK,  6);
                break;
            case 13:
                m.put(Balloon.BLUE,   5);
                m.put(Balloon.GREEN,  5);
                m.put(Balloon.BLACK,  7);
                break;
            case 14:
                m.put(Balloon.ZEPPLIN, 1);
                break;
            case 15:
                m.put(Balloon.BLACK,  4);
                m.put(Balloon.ZEPPLIN, 1);
                break;
            case 16:
                m.put(Balloon.ZEPPLIN, 2);
                break;
            case 17:
                m.put(Balloon.GREEN,  10);
                m.put(Balloon.BLACK,  4);
                m.put(Balloon.ZEPPLIN, 2);
                break;
            case 18:
                m.put(Balloon.ZEPPLIN, 3);
                break;
            case 19:
                m.put(Balloon.RED,   5);
                m.put(Balloon.BLUE,   5);
                m.put(Balloon.GREEN,  5);
                m.put(Balloon.BLACK,  5);
                m.put(Balloon.ZEPPLIN, 3);
                break;
            case 20:
                m.put(Balloon.ZEPPLINBLACK, 2);
                break;
            default: m.put(Balloon.RED, 20);                          break;
        }
    }
    private void setupHard(int p, Map<Balloon,Integer> m){
        switch(p){
            case 1:
                m.put(Balloon.RED,    1);
                break;
            case 2:
                m.put(Balloon.RED,    12);
                break;
            case 3:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,    3);
                break;
            case 4:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,    5);
                break;
            case 5:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   5);
                m.put(Balloon.GREEN,   3);
                break;
            case 6:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,   5);
                break;
            case 7:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,   10);
                break;
            case 8:
                m.put(Balloon.RED,    10);
                m.put(Balloon.BLUE,   10);
                m.put(Balloon.GREEN,  20);
                break;
            case 9:
                m.put(Balloon.RED,    5);
                m.put(Balloon.BLUE,   15);
                m.put(Balloon.GREEN,  25);
                break;
            case 10:
                m.put(Balloon.BLUE,   20);
                m.put(Balloon.GREEN,  20);
                break;
            case 11:
                m.put(Balloon.BLUE,   15);
                m.put(Balloon.BLACK,  3);
                break;
            case 12:
                m.put(Balloon.GREEN,  10);
                m.put(Balloon.BLACK,  6);
                break;
            case 13:
                m.put(Balloon.BLUE,   5);
                m.put(Balloon.GREEN,  5);
                m.put(Balloon.BLACK,  7);
                break;
            case 14:
                m.put(Balloon.BLACK,  2);
                m.put(Balloon.ZEPPLIN, 1);
                break;
            case 15:
                m.put(Balloon.BLACK,  5);
                m.put(Balloon.ZEPPLIN, 1);
                break;
            case 16:
                m.put(Balloon.BLACK,  5);
                m.put(Balloon.ZEPPLIN, 2);
                break;
            case 17:
                m.put(Balloon.GREEN,  10);
                m.put(Balloon.BLACK,  7);
                m.put(Balloon.ZEPPLIN, 2);
                break;
            case 18:
                m.put(Balloon.BLACK,  6);
                m.put(Balloon.ZEPPLIN, 3);
                break;
            case 19:
                m.put(Balloon.RED,   5);
                m.put(Balloon.BLUE,   5);
                m.put(Balloon.GREEN,  5);
                m.put(Balloon.BLACK,  9);
                m.put(Balloon.ZEPPLIN, 3);
                break;
            case 20:
                m.put(Balloon.BLACK,  2);
                m.put(Balloon.ZEPPLIN, 2);
                m.put(Balloon.ZEPPLINBLACK, 2);
                break;
            default: m.put(Balloon.RED, 20);                          break;
        }
    }

    private ArrayList<BalloonEnemy> createBalloons(Map<Balloon,Integer> cfg){
        ArrayList<BalloonEnemy> list = new ArrayList<>();
        for (Map.Entry<Balloon,Integer> e : cfg.entrySet()) {
            for (int i=0; i<e.getValue(); i++) {
                list.add(new BalloonEnemy(context, e.getKey(), new Point(0,0)));
            }
        }
        return list;
    }
}