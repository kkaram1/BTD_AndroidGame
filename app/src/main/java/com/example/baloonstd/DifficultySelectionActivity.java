package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;

public class DifficultySelectionActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_select);

        findViewById(R.id.btnEasy).setOnClickListener(v -> startGame(Difficulty.EASY));
        findViewById(R.id.btnMedium).setOnClickListener(v -> startGame(Difficulty.MEDIUM));
        findViewById(R.id.btnHard).setOnClickListener(v -> startGame(Difficulty.HARD));
    }

    private void startGame(Difficulty diff) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("mapNum", getIntent().getIntExtra("mapNum", 0)); // of waar je het vandaan haalt
        i.putExtra("difficulty", diff.name());
        startActivity(i);
        finish();
    }
}

