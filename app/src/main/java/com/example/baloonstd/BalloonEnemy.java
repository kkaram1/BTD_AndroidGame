package com.example.baloonstd;

import android.graphics.Bitmap;
import android.graphics.Point;

public class BalloonEnemy {
    Bitmap balloonImage;
    private float speedPixelsPerSecond;
    int layer;
    Point position;
    int currentWaypointIndex;
    BalloonEnemy(Bitmap balloonImage, float speedPixelsPerSecond, int layer, Point position) {
        this.balloonImage = balloonImage;
        this.speedPixelsPerSecond = speedPixelsPerSecond;
        this.layer = layer;
        this.position = position;
        currentWaypointIndex = 0;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public float getSpeedPixelsPerSecond() {
        return speedPixelsPerSecond;
    }
}
