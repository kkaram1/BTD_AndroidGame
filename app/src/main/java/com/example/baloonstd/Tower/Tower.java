package com.example.baloonstd.Tower;

import android.content.Context;
import android.graphics.Rect;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.baloonstd.UpgradeType;

import java.util.HashMap;
import java.util.Map;

public class Tower extends AppCompatImageView {
    private final Towers towerType;
    private int radius;
    private final long originalCooldown;
    private long shotCooldown;
    private final Map<UpgradeType, Integer> upgradeLevels;

    public Tower(Context context, Towers towerType) {
        super(context);
        this.towerType = towerType;
        this.radius = towerType.getRange();
        this.originalCooldown = towerType.getShotCooldownMs();
        this.shotCooldown = originalCooldown;
        this.upgradeLevels = new HashMap<>();
        setImageResource(towerType.getResourceId());
        setClickable(true);
    }

    public Towers getTowerType() {
        return towerType;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public long getShotCooldown() {
        return shotCooldown;
    }

    public void setShotCooldown(long ms) {
        this.shotCooldown = ms;
    }

    public long getOriginalCooldown() {
        return originalCooldown;
    }

    public Rect getBounds() {
        int left = (int) getX();
        int top = (int) getY();
        return new Rect(left, top, left + getWidth(), top + getHeight());
    }

    public int getUpgradeLevel(UpgradeType type) {
        return upgradeLevels.getOrDefault(type, 0);
    }

    public void incrementUpgrade(UpgradeType type) {
        upgradeLevels.put(type, getUpgradeLevel(type) + 1);
    }
}
