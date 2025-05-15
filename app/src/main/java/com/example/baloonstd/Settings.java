package com.example.baloonstd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.settings);
        SeekBar musicSeekBar = findViewById(R.id.musicVolumeSeekBar);
        SharedPreferences sharedPreferences = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);
        int savedVolume = sharedPreferences.getInt("musicVolume", 50); // default to 50
        musicSeekBar.setProgress(savedVolume);
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                MusicManager.setVolume(volume);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("musicVolume", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        SwitchCompat vibrationSwitch = findViewById(R.id.vibrationSwitch);
        boolean vibrationEnabled = sharedPreferences.getBoolean("vibrationEnabled", true);
        vibrationSwitch.setChecked(vibrationEnabled);

        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("vibrationEnabled", isChecked);
            editor.apply();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void goToLogin(View v){
        SharedPreferences prefs1 = getSharedPreferences("player_session", MODE_PRIVATE);
        prefs1.edit()
                .putString("username", null)
                .apply();
        Intent i = new Intent(Settings.this, LoginActivity.class);
        startActivity(i);
    }
}
