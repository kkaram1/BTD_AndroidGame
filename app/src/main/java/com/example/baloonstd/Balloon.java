package com.example.baloonstd;

import android.graphics.Point;

public enum Balloon {
    RED(R.drawable.red_balloon, 1200f, 1),BLUE(R.drawable.blue_balloon_correct, 1400f, 2)
    ,GREEN(R.drawable.red_balloon, 1600f, 3);

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

    public Point getPosition() {return position;}

    public void setPosition(Point position) {
        this.position = position;
    }
}
