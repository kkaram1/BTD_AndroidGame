package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baloonstd.MusicManager;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        MusicManager.start(this, R.raw.main_menu_music);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToMenu(View v) {
        startActivity(new Intent(this, MenuSelect.class));
    }

    public void goToSettings(View v) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void goToLeaderBoard(View v) {
        Intent intent = new Intent(this, LeaderBoard.class);
        startActivity(intent);
    }
}