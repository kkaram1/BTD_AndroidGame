package com.example.baloonstd;

import android.graphics.Bitmap;
import android.graphics.Point;

public class BalloonEnemy {
    Bitmap balloonImage;
    float speed;
    int layer;
    Point position;
    int currentWaypointIndex;
    BalloonEnemy(Bitmap balloonImage, float speed, int layer, Point position) {
        this.balloonImage = balloonImage;
        this.speed = speed;
        this.layer = layer;
        this.position = position;
        currentWaypointIndex = 1;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
