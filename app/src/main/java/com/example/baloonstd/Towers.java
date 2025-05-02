package com.example.baloonstd;

public enum Towers {
    DART_MONKEY(1, R.drawable.angrymonkey, "Dart Monkey", 250,1000),
    SNIPER_MONKEY(2, R.drawable.sniper, "Sniper Monkey", 9000,1000),
    ICE_MONKEY(3, R.drawable.dartmonkey, "Ice Monkey", 180,1000);

    private final int tag;
    private final String displayName;
    private final int range;
    private final int resourceId;
    private long ShotCooldownMs;

    Towers(int tag, int resourceId, String displayName, int range,long ShotCooldownMs) {
        this.tag = tag;
        this.resourceId = resourceId;
        this.displayName = displayName;
        this.range = range;
        this.ShotCooldownMs=ShotCooldownMs;
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