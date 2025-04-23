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

            nextPhaseButton.setVisibility(View.GONE);
            phaseAnnouncement.setText("Phase 2");

            gameView.setPhase(2, 30);
        });

        FrameLayout container = findViewById(R.id.gameContainer);
        gameView = new GameView(this);
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
}
