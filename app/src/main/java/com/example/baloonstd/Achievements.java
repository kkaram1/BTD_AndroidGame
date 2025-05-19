package com.example.baloonstd;

import android.os.Build;

import java.time.Instant;
import java.util.function.Predicate;
import com.example.baloonstd.Player.Player;

/**
 * Represents a single achievement, with an unlock condition based on Player.
 */
public class Achievements {
    private final int       id;
    private final String    name;
    private final String    description;
    private final Predicate<Player> condition;
    private Instant unlockedAt;
    public Achievements(int id,
                        String name,
                       String description,
                        Predicate<Player> condition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    /**
     * @return true if, given the current player, this achievement should unlock.
     */
    public boolean shouldUnlock( Player player) {
        return condition.test(player);
    }

    /**
     * Marks this achievement as unlocked right now.
     */
    public void markUnlocked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.unlockedAt = Instant.now();
        }
    }

    /**
     * @return The timestamp when this was unlocked, or null if not yet unlocked.
     */
    public Instant getUnlockedAt() {
        return unlockedAt;
    }

    /**
     * @return true if this has already been unlocked locally.
     */
    public boolean isUnlocked() {
        return unlockedAt != null;
    }
}
