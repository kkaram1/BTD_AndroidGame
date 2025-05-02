// gameActivity.java
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
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

import com.example.baloonstd.Phase.PhaseManager;

public class gameActivity extends AppCompatActivity {
    private LinearLayout towerPanel;
    private TextView phaseAnnouncement;
    private Button nextPhaseButton;
    private GameView gameView;
    private ImageView mapImageView;
    private PhaseManager phaseManager;

    private TextView moneyText;
    private int money = 100;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        mapImageView      = findViewById(R.id.mapImageView);
        towerPanel        = findViewById(R.id.towerPanel);
        Button openPanel  = findViewById(R.id.towerButton);
        phaseAnnouncement = findViewById(R.id.phaseAnnouncement);
        nextPhaseButton   = findViewById(R.id.nextPhaseButton);
        moneyText         = findViewById(R.id.moneyText);

        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        FrameLayout dragLayer     = findViewById(R.id.dragLayer);
        ImageView towerMonkeyIcon = findViewById(R.id.towerMonkeyIcon);
        ImageView towerSniperIcon = findViewById(R.id.towerSniperIcon);

        Intent intent = getIntent();
        int mapNum = intent.getIntExtra("mapNum", -1);
        gameView = new GameView(this, mapNum);
        phaseManager = new PhaseManager(this);
        gameView.setPhase(phaseManager);
        gameContainer.addView(gameView);
        updateMapImage(mapNum);

        updateMoney(0);

        DragDropController controller = new DragDropController(
                dragLayer,
                towerPanel,
                towerMonkeyIcon,
                towerSniperIcon,
                this
        );
        controller.init();

        openPanel.setOnClickListener(v -> {
            boolean vis = towerPanel.getVisibility() == LinearLayout.VISIBLE;
            towerPanel.setVisibility(vis ? LinearLayout.GONE : LinearLayout.VISIBLE);
        });

        nextPhaseButton.setOnClickListener(v -> {
            if (phaseManager.hasNextPhase()) {
                phaseManager.moveToNextPhase();
                gameView.setPhase(phaseManager);
                phaseAnnouncement.setText("Phase " + phaseManager.getCurrentPhaseNum());
                nextPhaseButton.setVisibility(Button.GONE);
            } else {
                phaseAnnouncement.setText("No more phases!");
                nextPhaseButton.setVisibility(Button.GONE);
            }
        });

        gameView.setOnPhaseCompleteListener(phase -> runOnUiThread(() -> {
            phaseAnnouncement.setText("End of Phase " + phase);
            nextPhaseButton.setText("Start Next Phase");
            nextPhaseButton.setVisibility(Button.VISIBLE);
        }));

        gameView.setOnBalloonEscapeListener(() -> runOnUiThread(() -> {
            // lost life logic
        }));

        gameView.setOnBalloonPopListener(() -> runOnUiThread(() -> {
            addMoney(5);
        }));
    }

    public boolean spendMoney(int amount) {
        if (money < amount) return false;
        money -= amount;
        updateMoney(0);
        return true;
    }

    public GameView getGameView() {
        return gameView;
    }
    public void addMoney(int amount) {
        money += amount;
        updateMoney(amount);
    }

    private void updateMoney(int delta) {
        moneyText.setText("Money: " + money);
    }

    private void updateMapImage(int mapNum) {
        switch (mapNum) {
            case 0: mapImageView.setImageResource(R.drawable.btdmap1); break;
            case 1: mapImageView.setImageResource(R.drawable.btdmap2); break;
            case 2: mapImageView.setImageResource(R.drawable.red_balloon); break;
        }
    }
}
