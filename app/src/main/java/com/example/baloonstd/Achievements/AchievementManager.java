package com.example.baloonstd.Achievements;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.baloonstd.Player.Player;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AchievementManager {
    private static AchievementManager instance;
    private final List<Achievements> all;
    private final Set<Integer> unlockedIds = new HashSet<>();
    private final SharedPreferences prefs;
    private Context context;
    private AchievementManager(Context ctx) {
        context = ctx.getApplicationContext();
        prefs = context.getSharedPreferences("player_session", Context.MODE_PRIVATE);
        all = AchievementRepository.getAll();
        loadUnlockedFromPrefs();
    }

    public static void init(Context ctx) {
        if (instance == null) {
            instance = new AchievementManager(ctx);
        }
    }

    public static AchievementManager get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "AchievementManager not initialized—call init(ctx) first");
        }
        return instance;
    }


    public void checkAll(Player player) {
        for (Achievements a : all) {
            if (!unlockedIds.contains(a.getId()) && a.shouldUnlock(player)) {
                unlockedIds.add(a.getId());
                saveUnlockedToPrefs();
                onAchievementUnlocked(a);
            }
        }
    }

    private void onAchievementUnlocked(Achievements a) {
        Toast.makeText(context,"Achievement Unlocked! "+a.getName(),Toast.LENGTH_LONG).show();
    }

    private void loadUnlockedFromPrefs() {
        Set<String> saved = prefs.getStringSet("achievements", new HashSet<String>());
        for (String idStr : saved) {
            try {
                unlockedIds.add(Integer.valueOf(idStr));
            } catch (NumberFormatException e) {
            }
        }
    }
    private void saveUnlockedToPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> toSave = new HashSet<>();
        for (Integer id : unlockedIds) {
            toSave.add(Integer.toString(id));
        }
        editor.putStringSet("achievements", toSave);
        editor.apply();
    }
}
