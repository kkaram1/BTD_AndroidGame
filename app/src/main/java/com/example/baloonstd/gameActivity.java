package com.example.baloonstd;

import static com.example.baloonstd.Towers.DART_MONKEY;
import static com.example.baloonstd.Towers.SNIPER_MONKEY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Pair;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.baloonstd.Phase.PhaseManager;

import java.util.ArrayList;

public class gameActivity extends AppCompatActivity {
    private LinearLayout towerPanel;
    private Button nextPhaseButton;
    private GameView gameView;
    private ImageView mapImageView;
    private PhaseManager phaseManager;
    private ArrayList<Pair<Towers,ImageView>> pairList;
    private TextView moneyText;
    private int money = 95;
    private int health = 100;
    private TextView healthText;

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

        nextPhaseButton   = findViewById(R.id.nextPhaseButton);
        moneyText         = findViewById(R.id.moneyText);
        healthText = findViewById(R.id.health);
        updateHealthUI();

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
                nextPhaseButton.setVisibility(Button.GONE);
            } else {
                nextPhaseButton.setVisibility(Button.GONE);
            }
        });

        gameView.setOnPhaseCompleteListener(phase -> runOnUiThread(() -> {
            int phaseDispaly = phase +1;
            nextPhaseButton.setText("Start Phase "+ phaseDispaly);
            nextPhaseButton.setVisibility(Button.VISIBLE);
        }));

        gameView.setOnBalloonEscapeListener(layer ->
                runOnUiThread(() -> onBalloonReachedEnd(layer))
        );


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
        moneyText.setText( ""+money);

        for(Pair<Towers, ImageView> pair : pairList) {
            if (pair.first.getPrice() > money) {
                pair.second.setAlpha(0.5f); // make icon semi-transparent to show it's unaffordable
                pair.second.setEnabled(false); // optional: prevent clicking
            } else {
                pair.second.setAlpha(1.0f);
                pair.second.setEnabled(true);
            }
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
        moneyText.setText(""+ money);
    }

    private void updateMapImage(int mapNum) {
        switch (mapNum) {
            case 0: mapImageView.setImageResource(R.drawable.btdmap1); break;
            case 1: mapImageView.setImageResource(R.drawable.btdmap2); break;
            case 2: mapImageView.setImageResource(R.drawable.red_balloon); break;
        }
    }

    private void onBalloonReachedEnd(int damage) {
        health -= damage;       // subtract by layer
        updateHealthUI();
        if (health <= 0) {
            Intent intent = new Intent(this, GameOverActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void updateHealthUI() {
        healthText.setText("" + health);
    }
}
