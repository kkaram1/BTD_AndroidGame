package com.example.baloonstd;

import android.graphics.Point;

public class BalloonEnemy {
    int balloonImage;
    float speed;
    int health;
    Point position;
    int currentWaypointIndex;
    BalloonEnemy(int balloonImage, float speed, int health, Point position) {
        this.balloonImage = balloonImage;
        this.speed = speed;
        this.health = health;
        this.position = position;
        currentWaypointIndex = 1;
    }
}
