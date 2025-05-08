package com.example.baloonstd.Balloon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class BalloonEnemy {
    private Balloon type;
    private Bitmap balloonImage;
    private float speedPixelsPerSecond;
    private int layer;
    private Point position;
    private int currentWaypointIndex;

    public BalloonEnemy(Context ctx, Balloon type, Point position) {
        this.type = type;
        this.position = position;
        this.currentWaypointIndex = 0;
        applyType(ctx, type);
    }

    private void applyType(Context ctx, Balloon b) {
        this.type  = b;
        this.layer = b.getLayer();
        this.speedPixelsPerSecond = b.getSpeed();
        Bitmap raw = BitmapFactory.decodeResource(ctx.getResources(), b.getResourceId());
        this.balloonImage = Bitmap.createScaledBitmap(raw, 100, 100, true);
    }

    public float getSpeedPixelsPerSecond() {
        return speedPixelsPerSecond;
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
}
