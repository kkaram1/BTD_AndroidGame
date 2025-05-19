package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;

public class DifficultySelectionActivity extends BaseActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_select);

        findViewById(R.id.btnEasy).setOnClickListener(v -> goToMapSelect(Difficulty.EASY));
        findViewById(R.id.btnMedium).setOnClickListener(v -> goToMapSelect(Difficulty.MEDIUM));
        findViewById(R.id.btnHard).setOnClickListener(v -> goToMapSelect(Difficulty.HARD));
    }

    private void goToMapSelect(Difficulty diff) {
        Intent i = new Intent(this, MenuSelect.class);
        i.putExtra("difficulty", diff.name());
        startActivity(i);
        finish();
    }
}
