package com.example.baloonstd.Balloon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

import com.example.baloonstd.R;

public enum Balloon {
    RED(R.drawable.red_balloon, 120f, 1,100),BLUE(R.drawable.blue_balloon, 140f, 2,100)
    ,GREEN(R.drawable.green_balloon, 160f, 3,100),ZEPPLIN(R.drawable.zepplin,80f,10,300);

    private int resourceId;
    private float speed;
    private int layer;
    private Point position;
    private final int displaySize;

    private Bitmap cachedBitmap;
    Balloon(int resourceId, float speed, int layer,int displaySize) {
        this.resourceId = resourceId;
        this.speed = speed;
        this.layer = layer;
        this.position=position;
        this.displaySize = displaySize;
    }

    public Bitmap getBitmap(Context ctx) {
        if (cachedBitmap == null) {
            Bitmap raw = BitmapFactory.decodeResource(ctx.getResources(), resourceId);
            if (this == ZEPPLIN) {
                Matrix m = new Matrix();
                m.postRotate(90);
                raw = Bitmap.createBitmap(raw, 0, 0, raw.getWidth(), raw.getHeight(), m, true);
            }

            cachedBitmap = Bitmap.createScaledBitmap(raw, displaySize, displaySize, true);
            raw.recycle();
        }
        return cachedBitmap;
    }
    public int getResourceId() {
        return resourceId;
    }

    public float getSpeed() {
        return speed;
    }

    public int getLayer() {
        return layer;
    }


    public static Balloon fromLayer(int layer) {
        for (Balloon b : values()) {
            if (b.layer == layer) return b;
        }
        throw new IllegalArgumentException("Unknown layer: " + layer);
    }
}
