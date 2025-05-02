package com.example.baloonstd;

public enum Towers {
    DART_MONKEY("dart_monkey", R.drawable.angrymonkey, "Dart Monkey", 250,1000, 50),
    SNIPER_MONKEY("sniper_monkey", R.drawable.sniper, "Sniper Monkey", 9000,1000,100),
    ICE_MONKEY("ice_monkey", R.drawable.dartmonkey, "Ice Monkey", 180,1000,50);

    private final String tag;
    private final String displayName;
    private final int range;
    private final int resourceId;
    private final int price;
    private long ShotCooldownMs;



    Towers(String tag, int resourceId, String displayName, int range,long ShotCooldownMs, int price) {
        this.tag = tag;
        this.resourceId = resourceId;
        this.displayName = displayName;
        this.range = range;
        this.ShotCooldownMs=ShotCooldownMs;
        this.price=price;
    }

    public int getPrice(){
        return price;
    }
    public long getShotCooldownMs() {
        return ShotCooldownMs;
    }

    public void setShotCooldownMs(long shotCooldownMs) {
        ShotCooldownMs = shotCooldownMs;
    }

    public String getTag() {
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

    public static Towers fromTag(String tag) {
        for (Towers t : values()) {
            if (t.tag.equalsIgnoreCase(tag)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown tower tag: " + tag);
    }
}