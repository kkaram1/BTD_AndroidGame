package com.example.baloonstd.Balloon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;

public class BalloonEnemy {
    private Balloon type;
    private Bitmap balloonImage;
    private float speedPixelsPerSecond;
    private int layer;
    private Point position;
    private int currentWaypointIndex;
    private int hitsRemaining;

    private boolean frozen = false;
    private Bitmap originalImage;
    private float originalSpeed;

    private int damageLayers = 0;

    public BalloonEnemy(Context ctx, Balloon type, Point position) {
        this.type = type;
        this.position = position;
        this.currentWaypointIndex = 0;
        applyType(ctx, type);
        this.hitsRemaining = (type == Balloon.ZEPPLIN ? type.getLayer() : 1);
    }

    private void applyType(Context ctx, Balloon b) {
        this.type = b;
        this.layer = b.getLayer();
        this.speedPixelsPerSecond = b.getSpeed();
        this.balloonImage = b.getBitmap(ctx,false);
        this.originalImage = balloonImage;
        this.originalSpeed = speedPixelsPerSecond;
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

    public void incCurrentWayPointIndex(int x) {
        currentWaypointIndex += x;
    }

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

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze(Context ctx) {
        if (frozen) return;

        frozen = true;
        speedPixelsPerSecond = originalSpeed * 0.3f;

        // Load frozen image
        int frozenResId = type.getFrozenResId();
        if (frozenResId != 0) {
            Bitmap raw = BitmapFactory.decodeResource(ctx.getResources(), frozenResId);
            balloonImage = Bitmap.createScaledBitmap(raw, balloonImage.getWidth(), balloonImage.getHeight(), true);
        }

        new Handler().postDelayed(() -> {
            balloonImage = originalImage;
            speedPixelsPerSecond = originalSpeed;
            frozen = false;
        }, 5000);
    }


    public int getDamageLayers() {
        return damageLayers;
    }

    public void setDamageLayers(int dmg) {
        this.damageLayers = Math.max(0, dmg);
    }

}
