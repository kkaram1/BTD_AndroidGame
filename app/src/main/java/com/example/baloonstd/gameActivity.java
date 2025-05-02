package com.example.baloonstd;

import static com.example.baloonstd.Towers.DART_MONKEY;
import static com.example.baloonstd.Towers.SNIPER_MONKEY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Pair;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

import com.example.baloonstd.Phase.PhaseManager;

import java.util.ArrayList;

public class gameActivity extends AppCompatActivity {
    private LinearLayout towerPanel;
    private TextView phaseAnnouncement;
    private Button nextPhaseButton;
    private GameView gameView;
    private ImageView mapImageView;
    private PhaseManager phaseManager;

    private TextView moneyText;
    private int money = 95;

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
        towerMonkeyIcon   = findViewById(R.id.towerMonkeyIcon);
        towerSniperIcon   = findViewById(R.id.towerSniperIcon);

        FrameLayout gameContainer = findViewById(R.id. gameContainer);
        FrameLayout dragLayer     = findViewById(R.id.dragLayer);
        pairList = new ArrayList<>();
        pairList.add( new Pair<>(DART_MONKEY,findViewById(R.id.towerMonkeyIcon)));
        pairList.add( new Pair<>(SNIPER_MONKEY,findViewById(R.id.towerSniperIcon)));

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
                pairList,
                this
        );
        controller.init();

        openPanel.setOnClickListener(v -> {
            updateMoneyDisplay();
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
        updateMoneyDisplay();
        return true;
    }

    private void updateMoneyDisplay() {
        moneyText.setText("Money: " + money);

        if (money < Towers.DART_MONKEY.getPrice()) {
            towerMonkeyIcon.setImageResource(R.drawable.greybasic);
        } else {
            towerMonkeyIcon.setImageResource(Towers.DART_MONKEY.getResourceId());
        }

        if (money < Towers.SNIPER_MONKEY.getPrice()) {
            towerSniperIcon.setImageResource(R.drawable.greyedsnipercorr);
        } else {
            towerSniperIcon.setImageResource(Towers.SNIPER_MONKEY.getResourceId());
        }
    }


    public GameView getGameView() {
        return gameView;
    }
    public void addMoney(int amount) {
        money += amount;
        updateMoney(amount);
        updateMoneyDisplay();
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
