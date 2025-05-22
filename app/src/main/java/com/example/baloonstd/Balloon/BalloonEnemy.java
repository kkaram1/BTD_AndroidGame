package com.example.baloonstd.Balloon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.Map;

public class BalloonEnemy {

    private static final Map<Integer, Bitmap> frozenImageCache = new HashMap<>();
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
    private boolean destroyed = false;

    public BalloonEnemy(Context ctx, Balloon type, Point position) {
        this.type = type;
        this.position = position;
        this.currentWaypointIndex = 0;
        applyType(ctx, type);
        this.hitsRemaining = (type == Balloon.ZEPPLIN || type == Balloon.ZEPPLINBLACK || type == Balloon.BLACK)  ? type.getLayer() : 1;
    }

    private void applyType(Context ctx, Balloon b) {
        this.type = b;
        this.layer = b.getLayer();
        this.speedPixelsPerSecond = b.getSpeed();
        this.balloonImage = b.getBitmap(ctx,false);
        if (!frozen) {
            this.originalImage = balloonImage;
            this.originalSpeed = speedPixelsPerSecond;
        }
    }

    private void setFrozenImage(Context ctx) {
        int frozenResId = type.getFrozenResId();
        if (frozenResId != 0) {
            Bitmap cached = frozenImageCache.get(frozenResId);
            if (cached == null) {
                Bitmap raw = BitmapFactory.decodeResource(ctx.getResources(), frozenResId);
                cached = Bitmap.createScaledBitmap(raw, balloonImage.getWidth(), balloonImage.getHeight(), true);
                frozenImageCache.put(frozenResId, cached);
            }
            balloonImage = cached;
        }
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
            boolean wasFrozen = frozen;
            applyType(ctx, next);
            if (wasFrozen) {
                speedPixelsPerSecond = originalSpeed * 0.3f;
                setFrozenImage(ctx);
            }
        }
    }


    public boolean applyHit() {
        hitsRemaining--;
        return hitsRemaining <= 0;
    }

    public Balloon getType() {
        return type;
    }

    public void freeze(Context ctx) {
        if (frozen) return;

        frozen = true;
        speedPixelsPerSecond = originalSpeed * 0.3f;

        setFrozenImage(ctx);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            balloonImage = originalImage;
            speedPixelsPerSecond = originalSpeed;
            frozen = false;
        }, 5000);
    }
    public boolean isDestroyed() {
        return destroyed;
    }

    public void markDestroyed() {
        this.destroyed = true;
    }
}
