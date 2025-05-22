package com.example.baloonstd.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.baloonstd.R;

public class SFXManager {
    private static SoundPool soundPool;
    private static int popSoundId;
    private static float volume = 1f;

    public static void init(Context ctx) {
        if (soundPool != null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .build();
        } else {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }
        popSoundId = soundPool.load(ctx, R.raw.popc, 1);
    }
    public static void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v));
    }
    public static void playPop() {
        if (soundPool != null) {
            soundPool.play(popSoundId, volume, volume, 1, 0, 1f);
        }
    }
}