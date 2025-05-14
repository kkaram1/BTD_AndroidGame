package com.example.baloonstd;

import static com.example.baloonstd.Tower.Towers.DART_MONKEY;
import static com.example.baloonstd.Tower.Towers.SNIPER_MONKEY;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.baloonstd.Tower.Tower;
import  com.example.baloonstd.Tower.Towers;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Pair;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.view.WindowCompat;
import com.example.baloonstd.Phase.PhaseManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends BaseActivity {
    private LinearLayout towerPanel;
    private Button nextPhaseButton;
    private Button firstPhaseButton;
    private GameView gameView;
    private ImageView mapImageView;
    private PhaseManager phaseManager;
    private ArrayList<Pair<Towers,ImageView>> pairList;
    private TextView moneyText;
    private int money = 90;
    private int health = 50;
    private TextView healthText;
    private Button upgradeToggleButton;
    private int balloonsPopped;
    LinearLayout towerUpgradePopup;
    private  SharedPreferences prefs;
    Tower selectedTower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        mapImageView      = findViewById(R.id.mapImageView);
        towerPanel        = findViewById(R.id.towerPanel);
        Button openPanel  = findViewById(R.id.towerButton);
        ImageButton pauseButton = findViewById(R.id.pauseButton);
        nextPhaseButton   = findViewById(R.id.nextPhaseButton);
        firstPhaseButton  = findViewById(R.id.firstPhaseButton);
        moneyText         = findViewById(R.id.moneyText);
        healthText        = findViewById(R.id.health);
        Button resumeButton = findViewById(R.id.resumeButton);
        ImageButton closeButton = findViewById(R.id.btnCloseUpgrade);
        LinearLayout pauseMenu = findViewById(R.id.pauseMenu);
        Button exitButton = findViewById(R.id.exitButton);
        updateHealthUI();
        upgradeToggleButton  = findViewById(R.id.upgradeToggleButton);
        towerUpgradePopup   = findViewById(R.id.towerUpgradePopup);
        Button btnUpgradeRange = findViewById(R.id.btnUpgradeRange);
        LinearLayout gameWonScreen = findViewById(R.id.gameWonScreen);
        Button exitMain = findViewById(R.id.btnExitToMainMenu);
        Button endless = findViewById(R.id.btnEndlessMode);
        upgradeToggleButton.setVisibility(View.GONE);
        prefs  = getSharedPreferences("player_session", MODE_PRIVATE);
        balloonsPopped = prefs.getInt("balloonsPopped",0);


        upgradeToggleButton.setOnClickListener(v -> {
            boolean vis = towerUpgradePopup.getVisibility() == View.VISIBLE;
            towerUpgradePopup.setVisibility(vis ? View.GONE : View.VISIBLE);
        });
        btnUpgradeRange.setOnClickListener(v -> {
            if (selectedTower == null) return;
            int cost = 50;
            if (!spendMoney(cost)) {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedTower.setRadius(selectedTower.getRadius() + 70);
            selectedTower.setShotCooldown(800);
            Toast.makeText(this, "Range increased", Toast.LENGTH_SHORT).show();
            towerUpgradePopup.setVisibility(View.GONE);
            upgradeToggleButton.setVisibility(View.GONE);
            updateRangeOverlayFor(selectedTower);
        });

        FrameLayout gameContainer = findViewById(R.id. gameContainer);
        FrameLayout dragLayer     = findViewById(R.id.dragLayer);
        pairList = new ArrayList<>();
        pairList.add( new Pair<>(DART_MONKEY,findViewById(R.id.towerMonkeyIcon)));
        pairList.add( new Pair<>(SNIPER_MONKEY,findViewById(R.id.towerSniperIcon)));

        Intent intent = getIntent();
        int mapNum = intent.getIntExtra("mapNum", -1);
        gameView = new GameView(this, mapNum);
        phaseManager = new PhaseManager(this);
        gameContainer.addView(gameView);
        updateMapImage(mapNum);
        updateMoney(0);

        DragDropController controller = new DragDropController(
                dragLayer,
                towerPanel,
                pairList,
                towerUpgradePopup,
                closeButton,
                this
        );
        controller.init();

        firstPhaseButton.setOnClickListener(v3 -> {
            gameView.setPhase(phaseManager);
            firstPhaseButton.setVisibility(LinearLayout.GONE);
        });

        pauseButton.setOnClickListener(v -> {
            pauseMenu.setVisibility(LinearLayout.VISIBLE);
            gameView.setPaused(true); // Assuming GameView has a pause flag
        });

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
            saveBalloonPop();
            if(phase == 2){gameWonScreen.setVisibility(LinearLayout.VISIBLE);}
            int phaseDispaly = phase +1;
            nextPhaseButton.setText("Start Phase "+ phaseDispaly);
            nextPhaseButton.setVisibility(Button.VISIBLE);
        }));

        gameView.setOnBalloonEscapeListener(layer ->
                runOnUiThread(() -> onBalloonReachedEnd(layer))
        );


        gameView.setOnLayerPopListener(layer ->
                runOnUiThread(() -> {
                    addMoney(2 * layer);
                    balloonsPopped += layer;
                })
        );
        endless.setOnClickListener(v -> {
            gameWonScreen.setVisibility(LinearLayout.GONE);
            int phaseDispaly = 3;
            nextPhaseButton.setText("Start Phase "+ phaseDispaly);
            nextPhaseButton.setVisibility(Button.VISIBLE);
        });
        resumeButton.setOnClickListener(v -> {
            pauseMenu.setVisibility(LinearLayout.GONE);
            gameView.setPaused(false);
        });


        exitButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);
            finish();
        });
        exitMain.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);
            finish();
        });

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
                pair.second.setEnabled(false); // prevent clicking
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
    public int getMoney(){
        return money;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        LinearLayout pauseMenu = findViewById(R.id.pauseMenu);
        if (pauseMenu.getVisibility() != LinearLayout.VISIBLE) {
            pauseMenu.setVisibility(LinearLayout.VISIBLE);
            gameView.setPaused(true);
        }
    }


    public void updateRangeOverlayFor(Tower tower) {
        for (int i = 0; i < ((FrameLayout)findViewById(R.id.dragLayer)).getChildCount(); i++) {
            View child = ((FrameLayout)findViewById(R.id.dragLayer)).getChildAt(i);
            if (child instanceof RangeView) {
                float cx = tower.getX() + tower.getWidth()/2f;
                float cy = tower.getY() + tower.getHeight()/2f;

                float rvx = child.getX() + ((RangeView) child).getRadius();
                float rvy = child.getY() + ((RangeView) child).getRadius();
                if (Math.hypot(cx - rvx, cy - rvy) < 1f) {
                    RangeView rv = (RangeView) child;
                    rv.setRadius(tower.getRadius());
                    float r = rv.getRadius();
                    rv.setX(cx - r);
                    rv.setY(cy - r);
                    break;
                }
            }
        }
    }
    private void saveBalloonPop() {
        prefs.edit().putInt("balloonsPopped",balloonsPopped).apply();
        String url = "https://studev.groept.be/api/a24pt301/incBalloons";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                },
                error -> Toast.makeText(this, "Volley error: (network)" + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("a", String.valueOf(balloonsPopped));
                params.put("b", PlayerManager.getInstance().getUsername());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
