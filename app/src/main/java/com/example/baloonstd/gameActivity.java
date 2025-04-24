package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class gameActivity extends AppCompatActivity {
    private LinearLayout towerPanel;
    private boolean panelVisible = false;
    private TextView phaseAnnouncement;
    private Button nextPhaseButton;
    private GameView gameView;
    private ImageView mapImageView;
    private int mapNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_game);
        mapImageView = findViewById(R.id.mapImageView);

        // Get the mapNum from the Intent
        Intent intent = getIntent();
        mapNum = intent.getIntExtra("mapNum", -1); // Default to -1 if no mapNum is passed

        // Update the map image based on mapNum
        updateMapImage(mapNum);

        towerPanel = findViewById(R.id.towerPanel);
        Button openPanelButton = findViewById(R.id.towerButton);
        openPanelButton.setOnClickListener(v -> togglePanel());

        phaseAnnouncement = findViewById(R.id.phaseAnnouncement);
        nextPhaseButton = findViewById(R.id.nextPhaseButton);
        nextPhaseButton.setVisibility(View.GONE);
        nextPhaseButton.setOnClickListener(v -> {

            nextPhaseButton.setVisibility(View.GONE);
            phaseAnnouncement.setText("Phase 2");

            gameView.setPhase(2, 30);
        });

        FrameLayout container = findViewById(R.id.gameContainer);
        gameView = new GameView(this,mapNum);
        container.addView(gameView);


        gameView.setOnPhaseCompleteListener(phase -> {
            if (phase == 1) {
                // Phase 1 complete: update announcement and show next-phase button.
                runOnUiThread(() -> {
                    phaseAnnouncement.setText("End Phase 1");
                    nextPhaseButton.setVisibility(View.VISIBLE);
                });
            }

        });

        gameView.setPhase(1, 20);
    }

    public void togglePanel() {
        panelVisible = !panelVisible;
        towerPanel.setVisibility(panelVisible ? View.VISIBLE : View.GONE);
    }
    private void updateMapImage(int mapNum) {
        switch (mapNum) {
            case 0:
                mapImageView.setImageResource(R.drawable.btd1_map); // Map 0
                break;
            case 1:
                mapImageView.setImageResource(R.drawable.
                        homepage_background); // Map 1
                break;
            case 2:
                mapImageView.setImageResource(R.drawable.red_balloon); // Map 2
                break;}
    }
}

