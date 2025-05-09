package com.example.baloonstd.Balloon;

import android.graphics.Point;

import com.example.baloonstd.R;

public enum Balloon {
    RED(R.drawable.red_balloon, 120f, 1),BLUE(R.drawable.blue_balloon, 140f, 2)
    ,GREEN(R.drawable.green_balloon, 160f, 3),ZEPPLIN(R.drawable.zepplin,80f,10);

    private int resourceId;
    private float speed;
    private int layer;
    private Point position;


    Balloon(int resourceId, float speed, int layer) {
        this.resourceId = resourceId;
        this.speed = speed;
        this.layer = layer;
        this.position=position;
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
