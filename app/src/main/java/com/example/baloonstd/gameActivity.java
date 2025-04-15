package com.example.baloonstd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge content
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_game);

        towerPanel = findViewById(R.id.towerPanel);
        Button openPanelButton = findViewById(R.id.towerButton);
        openPanelButton.setOnClickListener(v -> togglePanel());

        phaseAnnouncement = findViewById(R.id.phaseAnnouncement);
        nextPhaseButton = findViewById(R.id.nextPhaseButton);
        nextPhaseButton.setVisibility(View.GONE);
        nextPhaseButton.setOnClickListener(v -> {
            // Start Phase 2 when button is pressed.
            nextPhaseButton.setVisibility(View.GONE);
            phaseAnnouncement.setText("Phase 2");
            // For example, phase 2 spawns 30 balloons.
            gameView.setPhase(2, 30);
        });

        FrameLayout container = findViewById(R.id.gameContainer);
        gameView = new GameView(this);
        container.addView(gameView);

        // Set a listener for phase completion
        gameView.setOnPhaseCompleteListener(phase -> {
            if (phase == 1) {
                // Phase 1 complete: update announcement and show next-phase button.
                runOnUiThread(() -> {
                    phaseAnnouncement.setText("End Phase 1");
                    nextPhaseButton.setVisibility(View.VISIBLE);
                });
            }
            // You can add logic for further phases as needed.
        });

        // Start phase 1 with, for example, 20 balloons.
        gameView.setPhase(1, 20);
    }

    public void togglePanel() {
        panelVisible = !panelVisible;
        towerPanel.setVisibility(panelVisible ? View.VISIBLE : View.GONE);
    }
}
