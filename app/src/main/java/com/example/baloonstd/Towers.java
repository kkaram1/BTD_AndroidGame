package com.example.baloonstd;

public enum Towers {
    DART_MONKEY(1, R.drawable.angrymonkey, "Dart Monkey", 250,1000,50,1000f),
    SNIPER_MONKEY(2, R.drawable.sniper, "Sniper Monkey", 9001,2000,100,5000f),
    ICE_MONKEY(3, R.drawable.dartmonkey, "Ice Monkey", 180,1000,75, 1000f);

    private final int tag;
    private final String displayName;
    private final int range;
    private final int resourceId;
    private final int price;
    private long ShotCooldownMs;
    private final float  bulletspeed;



    Towers(int tag, int resourceId, String displayName, int range,long ShotCooldownMs, int price, float bulletspeed) {
        this.tag = tag;
        this.resourceId = resourceId;
        this.displayName = displayName;
        this.range = range;
        this.ShotCooldownMs=ShotCooldownMs;
        this.price=price;
        this.bulletspeed=bulletspeed;
    }

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


}