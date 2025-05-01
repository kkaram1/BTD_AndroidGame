package com.example.baloonstd;

public enum Towers {
    DART_MONKEY("dart_monkey", R.drawable.dartmonkey, "Dart Monkey", 150),
    SNIPER_MONKEY("sniper_monkey", R.drawable.sniper, "Sniper Monkey", 300),
    ICE_MONKEY("ice_monkey", R.drawable.dartmonkey, "Ice Monkey", 180);

    private final String tag;
    private final String displayName;
    private final int range;
    private final int resourceId;

    Towers(String tag, int resourceId, String displayName, int range) {
        this.tag = tag;
        this.resourceId = resourceId;
        this.displayName = displayName;
        this.range = range;
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