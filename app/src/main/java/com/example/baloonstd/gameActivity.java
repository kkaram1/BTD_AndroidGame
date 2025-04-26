package com.example.baloonstd;

import android.annotation.SuppressLint;
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

    private ImageView towerMonkeyIcon;
    private FrameLayout gameContainer;
    private towerDrag dragController;

    private PhaseManager phaseManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        mapImageView = findViewById(R.id.mapImageView);
        towerPanel = findViewById(R.id.towerPanel);
        Button openPanel = findViewById(R.id.towerButton);
        phaseAnnouncement = findViewById(R.id.phaseAnnouncement);
        nextPhaseButton = findViewById(R.id.nextPhaseButton);

        gameContainer = findViewById(R.id.gameContainer);
        towerMonkeyIcon = findViewById(R.id.towerMonkeyIcon);

        Intent intent = getIntent();
        mapNum = intent.getIntExtra("mapNum", -1);
        gameView = new GameView(this, mapNum);
        phaseManager = new PhaseManager(this);
        gameView.setPhase(phaseManager);
        gameContainer.addView(gameView);
        updateMapImage(mapNum);

        dragController = new towerDrag(
                gameContainer,
                towerPanel,
                towerMonkeyIcon,
                gameView
        );
        dragController.init();

        openPanel.setOnClickListener(v -> togglePanel());
        nextPhaseButton.setVisibility(View.GONE);
        nextPhaseButton.setOnClickListener(v -> {
            if (phaseManager.hasNextPhase()) {
                phaseManager.moveToNextPhase();
                gameView.setPhase(phaseManager);
                nextPhaseButton.setVisibility(View.GONE);
                phaseAnnouncement.setText("Phase " + phaseManager.getCurrentPhaseNum());
            }
        });

        gameView.setOnPhaseCompleteListener(phase -> {
            runOnUiThread(() -> {
                phaseAnnouncement.setText("End Phase " + phase);
                nextPhaseButton.setVisibility(View.VISIBLE);
            });
        });
    }

    public void togglePanel() {
        panelVisible = !panelVisible;
        towerPanel.setVisibility(panelVisible ? View.VISIBLE : View.GONE);
    }

    private void updateMapImage(int mapNum) {
        switch (mapNum) {
            case 0:
                mapImageView.setImageResource(R.drawable.btdmap1);
                break;
            case 1:
                mapImageView.setImageResource(R.drawable.btdmap2);
                break;
            case 2:
                mapImageView.setImageResource(R.drawable.red_balloon);
                break;
        }
    }
}