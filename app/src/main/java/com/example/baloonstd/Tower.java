package com.example.baloonstd;

import android.content.Context;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatImageView;

public class Tower extends AppCompatImageView {
    private int radius;
    private final Towers towerType;

    public Tower(Context context, Towers towerType) {
        super(context);
        this.towerType = towerType;
        this.radius = towerType.getRange();
        setImageResource(towerType.getResourceId());
        setClickable(true);
    }

    public int getRadius() {
        return radius;
    }

    public Towers getTowerType() {
        return towerType;
    }

    public Rect getBounds() {
        int left = (int) getX();
        int top = (int) getY();
        return new Rect(left, top, left + getWidth(), top + getHeight());
    }
}