package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuSelect extends BaseActivity {
    private ImageButton map1;
    private ImageButton map2;
    private ImageButton map3;
    private CardView difficultyCard;
    private int mapNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_select);
        map1 = findViewById(R.id.map1);
        map2 = findViewById(R.id.map2);
        map3 = findViewById(R.id.map3);
        difficultyCard = findViewById(R.id.difficultyCard);
        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });
        map1.setOnClickListener(v -> {
            difficultyCard.setVisibility(View.VISIBLE);
            mapNum=0;
        });
        map2.setOnClickListener(v -> {
            difficultyCard.setVisibility(View.VISIBLE);
            mapNum=1;
        });
        map3.setOnClickListener(v -> {
            difficultyCard.setVisibility(View.VISIBLE);
            mapNum=2;
        });

    }

    public void startGameEasy(View v) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("mapNum", mapNum);
        i.putExtra("difficulty", Difficulty.EASY);
        startActivity(i);
    }
    public void startGameMed(View v) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("mapNum", mapNum);
        i.putExtra("difficulty", Difficulty.MEDIUM);
        startActivity(i);
    }
    public void startGameHard(View v) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("mapNum", mapNum);
        i.putExtra("difficulty", Difficulty.HARD);
        startActivity(i);
    }
}
