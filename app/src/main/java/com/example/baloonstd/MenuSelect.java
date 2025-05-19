package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuSelect extends BaseActivity {
    private String difficulty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_select);
        difficulty = getIntent().getStringExtra("difficulty");
        if (difficulty == null) {
            difficulty = Difficulty.MEDIUM.name();
        }

        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });
    }

    public void goToGame0(View v) { startGame(0); }
    public void goToGame1(View v) { startGame(1); }
    public void goToGame2(View v) { startGame(2); }
    private void startGame(int mapNum) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("mapNum", mapNum);
        i.putExtra("difficulty", difficulty);
        startActivity(i);
    }
}
