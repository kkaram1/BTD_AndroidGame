package com.example.baloonstd.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MusicManager {
    private static MediaPlayer player;
    private static int currentResId = -1;

    public static void start(Context context, int resId) {
        if (player != null && currentResId == resId && player.isPlaying()) return;
        stop();
        player = MediaPlayer.create(context, resId);
        currentResId = resId;
        player.setLooping(true);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int savedVolume = prefs.getInt("musicVolume", 50);
        float volume = savedVolume / 100f;
        player.setVolume(volume, volume);
        player.start();
    }

    public static void pause() {
        if (player != null && player.isPlaying()) player.pause();
    }

    public static void resume() {
        if (player != null && !player.isPlaying()) player.start();
    }

    public static void stop() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            currentResId = -1;
        }
    }

    public static void setVolume(float volume) {player.setVolume(volume, volume);}
}