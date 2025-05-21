package com.example.baloonstd.Tower;

import com.example.baloonstd.R;
import com.example.baloonstd.UpgradeType;

import java.util.List;
import java.util.Map;

public enum Towers {
    DART_MONKEY(1, R.drawable.angrymonkey, "Dart Monkey", 250,1000,100,1000f,R.drawable.dart,
            List.of(UpgradeType.RANGE, UpgradeType.SPEED),Map.of(UpgradeType.RANGE, new int[]{50, 100}, UpgradeType.SPEED, new int[]{75, 150})),
    SNIPER_MONKEY(2, R.drawable.sniper, "Sniper Monkey", 9001,2000,200,5000f,R.drawable.bullet,
            List.of(UpgradeType.SPEED, UpgradeType.DAMAGE), Map.of(UpgradeType.SPEED, new int[]{100, 200}, UpgradeType.DAMAGE, new int[]{75, 100})),
    ICE_MONKEY(3, R.drawable.ice_wizard, "Ice Monkey", 200,1000,150, 1000f,R.drawable.iceball,
            List.of(UpgradeType.DAMAGE, UpgradeType.RANGE), Map.of(UpgradeType.DAMAGE, new int[]{50, 100}, UpgradeType.RANGE, new int[]{60, 120})),
    BANK(4, R.drawable.bank, "Bank", 0,0,500, 0,-1,
            List.of(UpgradeType.GENERATION),Map.of(UpgradeType.GENERATION, new int[]{150, 200}));
    private final int tag;
    private final String displayName;
    private final int range;
    private final int resourceId;
    private final int price;
    private long ShotCooldownMs;
    private final float  bulletspeed;
    private final int projectileResId;
    private final List<UpgradeType> upgrades;
    private final Map<UpgradeType,int[]> costs;


    Towers(int tag, int resourceId, String displayName, int range,long ShotCooldownMs, int price, float bulletspeed,int projectileResId,List<UpgradeType> upgrades,
           Map<UpgradeType,int[]> costs) {
        this.tag = tag;
        this.resourceId = resourceId;
        this.displayName = displayName;
        this.range = range;
        this.ShotCooldownMs=ShotCooldownMs;
        this.price=price;
        this.bulletspeed=bulletspeed;
        this.projectileResId=projectileResId;
        this.upgrades = upgrades;
        this.costs    = costs;
    }

    public int getProjectileResId() { return projectileResId; }
    public float getBulletSpeed() { return bulletspeed; }
    public int getPrice(){
        return price;
    }
    public long getShotCooldownMs() {
        return ShotCooldownMs;
    }

    public void setShotCooldownMs(long shotCooldownMs) {
        ShotCooldownMs = shotCooldownMs;
    }

    public int getTag() {
        return tag;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRange() {
        return range;
    }
    public List<UpgradeType> getUpgrades() { return upgrades; }
    public int getUpgradeCost(UpgradeType type, int lvl) {
        int[] arr = costs.getOrDefault(type, new int[0]);
        return lvl < arr.length ? arr[lvl] : Integer.MAX_VALUE;
    }
    public boolean supports(UpgradeType type) {
        return upgrades.contains(type);
    }
    public int getMaxUpgradeLevel(UpgradeType u) {
        int[] arr = costs.getOrDefault(u, new int[0]);
        return arr.length;
    }

    public int getGenerationPerRound(int level) {
        if (this != BANK) return 0;
        switch (level) {
            case 0:  return 100;
            case 1:  return 150;
            case 2:  return 225;
            default: return 225;
        }
    }



}