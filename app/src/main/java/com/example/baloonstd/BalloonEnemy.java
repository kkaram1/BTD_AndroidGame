package com.example.baloonstd;

public class BalloonEnemy {
    float posX, posY;
    int currentWaypointIndex;
    BalloonEnemy(float startX, float startY) {
        posX = startX;
        posY = startY;
        currentWaypointIndex = 1;
    }
}
