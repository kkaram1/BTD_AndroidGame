package com.example.baloonstd.Balloon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

import com.example.baloonstd.R;

public enum Balloon {
    RED(R.drawable.red_balloon,R.drawable.icered, 120f, 1,100),BLUE(R.drawable.blue_balloon,R.drawable.iceblue, 140f, 2,100)
    ,GREEN(R.drawable.green_balloon,R.drawable.icegreen, 160f, 3,100),BLACK(R.drawable.blackballoon,R.drawable.blackice, 160f, 2,100),
    ZEPPLIN(R.drawable.zepplin,R.drawable.icezepplin,80f,20,300),ZEPPLINBLACK(R.drawable.zepplinblack,R.drawable.blackicez,60f,40,300);


    private float speed;
    private int layer;
    private final int displaySize;
    private final int resNormal, resIce;

    private Bitmap cachedNormal, cachedIce;
    Balloon(int resNormal, int resIce, float speed, int layer,int displaySize) {
        this.resNormal = resNormal;
        this.resIce = resIce;
        this.speed = speed;
        this.layer = layer;
        this.displaySize = displaySize;
    }

    public Bitmap getBitmap(Context ctx, boolean iced) {
        if (iced) {
            if (cachedIce == null) {
                cachedIce = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(ctx.getResources(), resIce),
                        displaySize, displaySize, true
                );
            }
            return cachedIce;
        } else {
            if (cachedNormal == null) {
                cachedNormal = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(ctx.getResources(), resNormal),
                        displaySize, displaySize, true
                );
            }
            return cachedNormal;
        }
    }
    public int getFrozenResId() {
        return resIce;
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
