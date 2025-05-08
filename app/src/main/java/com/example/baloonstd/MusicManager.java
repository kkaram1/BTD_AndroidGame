package com.example.baloonstd;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer player;
    private static int currentResId = -1;

    public static void start(Context context, int resId) {
        if (player != null && currentResId == resId && player.isPlaying()) return;

        stop(); // stop current if different or null
        player = MediaPlayer.create(context, resId);
        currentResId = resId;
        player.setLooping(true);
        player.setVolume(0.5f, 0.5f);
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
}