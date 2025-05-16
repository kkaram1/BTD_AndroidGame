package com.example.baloonstd.Tower;

import android.content.Context;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.baloonstd.UpgradeType;

import java.util.HashMap;
import java.util.Map;

public class Tower extends AppCompatImageView {
    private int radius;
    private final Towers towerType;
    private long shotCooldown;
    private final Map<UpgradeType,Integer> upgradeLevels = new HashMap<>();


    public Tower(Context context, Towers towerType) {
        super(context);
        this.towerType = towerType;
        shotCooldown=towerType.getShotCooldownMs();
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

    public long getShotCooldown() {return shotCooldown;}

    public void setRadius(int newRadius) {
        this.radius = newRadius;
    }

    public void setShotCooldown(long ms) {
        this.shotCooldown = ms;
        towerType.setShotCooldownMs(ms);
    }
    public int getUpgradeLevel(UpgradeType u) {
        return upgradeLevels.getOrDefault(u, 0);
    }
    public void incrementUpgrade(UpgradeType u) {
        upgradeLevels.put(u, getUpgradeLevel(u) + 1);
    }

}