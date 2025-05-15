package com.example.baloonstd.Balloon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

public class BalloonEnemy {
    private Balloon type;
    private Bitmap balloonImage;
    private float speedPixelsPerSecond;
    private int layer;
    private Point position;
    private int currentWaypointIndex;
    private int hitsRemaining;

    public BalloonEnemy(Context ctx, Balloon type, Point position) {
        this.type = type;
        this.position = position;
        this.currentWaypointIndex = 0;
        applyType(ctx, type);
        this.hitsRemaining = (type == Balloon.ZEPPLIN ? type.getLayer() : 1);

    }

    private void applyType(Context ctx, Balloon b) {
        this.type  = b;
        this.layer = b.getLayer();
        this.speedPixelsPerSecond = b.getSpeed();
        this.balloonImage = b.getBitmap(ctx);
    }

    public float getSpeedPixelsPerSecond() {
        return speedPixelsPerSecond;
    }
    public void setSpeedPixelsPerSecond(float speed) {
        this.speedPixelsPerSecond = speed;
    }
    public Bitmap getImage() {
        return balloonImage;
    }
    public int getLayer() {
        return layer;
    }

    public int getCurrentWaypointIndex() {
        return currentWaypointIndex;
    }
    public void setCurrentWaypointIndex(int idx) {
        this.currentWaypointIndex = idx;
    }
    public void incCurrentWayPointIndex(int x){currentWaypointIndex+=x;}

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void downgrade(Context ctx) {
        if (layer > 1) {
            Balloon next = Balloon.fromLayer(layer - 1);
            applyType(ctx, next);
        }
    }
    public boolean applyHit() {
        hitsRemaining--;
        return hitsRemaining <= 0;
    }
    public Balloon getType() {
        return type;
    }

}
