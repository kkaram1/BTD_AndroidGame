package com.example.baloonstd;

import static android.view.View.GONE;
import static com.example.baloonstd.Tower.Towers.DART_MONKEY;
import static com.example.baloonstd.Tower.Towers.SNIPER_MONKEY;
import static com.example.baloonstd.Tower.Towers.ICE_MONKEY;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.baloonstd.Player.PlayerManager;
import com.example.baloonstd.Tower.Tower;
import  com.example.baloonstd.Tower.Towers;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.Context;

public class GameActivity extends BaseActivity {
    private LinearLayout towerPanel;
    private Button nextPhaseButton;
    private Button firstPhaseButton;
    private GameView gameView;
    private ImageView mapImageView;
    private PhaseManager phaseManager;
    private ArrayList<Pair<Towers,ImageView>> pairList;
    private TextView moneyText;
    private int money = 700;
    private int health = 50;
    private TextView healthText;
    private Button upgradeToggleButton;
    private int balloonsPopped;
    LinearLayout towerUpgradePopup;
    private  SharedPreferences prefs;
    protected Tower selectedTower;
    private Button btnRange;
    private Button btnDamage;
    private Button btnSpeed;
    private FrameLayout upgradeRangeContainer;
    private FrameLayout upgradeDamageContainer;
    private FrameLayout upgradeSpeedContainer;
    private SoundPool soundPool;
    private int popSoundId;

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
        LinearLayout gameWonScreen = findViewById(R.id.gameWonScreen);
        Button exitMain = findViewById(R.id.btnExitToMainMenu);
        Button endless = findViewById(R.id.btnEndlessMode);
        upgradeToggleButton.setVisibility(GONE);
        prefs  = getSharedPreferences("player_session", MODE_PRIVATE);
        balloonsPopped = prefs.getInt("balloonsPopped",0);
        ImageButton btnSellTower = findViewById(R.id.btnSellTower);
        btnRange  = findViewById(R.id.btnUpgradeRange);
        btnDamage = findViewById(R.id.btnUpgradeDamage);
        btnSpeed  = findViewById(R.id.btnUpgradeSpeed);
        upgradeRangeContainer  = findViewById(R.id.upgradeRangeContainer);
        upgradeDamageContainer = findViewById(R.id.upgradeDamageContainer);
        upgradeSpeedContainer  = findViewById(R.id.upgradeSpeedContainer);

        TextView dartPriceTxt = findViewById(R.id.dartPrice);
        dartPriceTxt.setText(String.valueOf(Towers.DART_MONKEY.getPrice()));

        TextView sniperPriceTxt = findViewById(R.id.sniperPrice);
        sniperPriceTxt.setText(String.valueOf(Towers.SNIPER_MONKEY.getPrice()));

        TextView icePriceTxt = findViewById(R.id.icePrice);
        icePriceTxt.setText(String.valueOf(Towers.ICE_MONKEY.getPrice()));


        upgradeToggleButton.setOnClickListener(v -> {
            if (towerUpgradePopup.getVisibility() == View.VISIBLE) {
                towerUpgradePopup.setVisibility(GONE);
            } else if (selectedTower != null) {
                configurePopupFor(selectedTower);
                towerUpgradePopup.setVisibility(View.VISIBLE);
            }
        });



        FrameLayout gameContainer = findViewById(R.id. gameContainer);
        FrameLayout dragLayer     = findViewById(R.id.dragLayer);
        pairList = new ArrayList<>();
        pairList.add( new Pair<>(DART_MONKEY,findViewById(R.id.towerMonkeyIcon)));
        pairList.add( new Pair<>(SNIPER_MONKEY,findViewById(R.id.towerSniperIcon)));
        pairList.add( new Pair<>(ICE_MONKEY,findViewById(R.id.towerIceIcon)));

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
            firstPhaseButton.setVisibility(GONE);
        });

        pauseButton.setOnClickListener(v -> {
            pauseMenu.setVisibility(LinearLayout.VISIBLE);
            gameView.setPaused(true);
        });

        openPanel.setOnClickListener(v -> {
            updateMoneyDisplay();
            boolean vis = towerPanel.getVisibility() == LinearLayout.VISIBLE;
            towerPanel.setVisibility(vis ? GONE : LinearLayout.VISIBLE);
        });

        nextPhaseButton.setOnClickListener(v -> {
            if (phaseManager.hasNextPhase()) {
                phaseManager.moveToNextPhase();
                gameView.setPhase(phaseManager);
                nextPhaseButton.setVisibility(GONE);
            } else {
                nextPhaseButton.setVisibility(GONE);
            }
        });


        gameView.setOnPhaseCompleteListener(phase -> runOnUiThread(() -> {
            if(!PlayerManager.getInstance().getPlayer().isGuest()){saveBalloonPop();}
            if(phase == 8){gameWonScreen.setVisibility(LinearLayout.VISIBLE);}
            int phaseDispaly = phase +1;
            nextPhaseButton.setText("Start Phase "+ phaseDispaly);
            nextPhaseButton.setVisibility(Button.VISIBLE);
        }));

        gameView.setOnBalloonEscapeListener(layer ->
                runOnUiThread(() -> onBalloonReachedEnd(layer)
                )
        );


        gameView.setOnLayerPopListener(layer ->
                runOnUiThread(() -> {
                    addMoney(2 * layer);
                    balloonsPopped += layer;
                })
        );
        endless.setOnClickListener(v -> {
            gameWonScreen.setVisibility(GONE);
            int phaseDispaly = 3;
            nextPhaseButton.setText("Start Phase "+ phaseDispaly);
            nextPhaseButton.setVisibility(Button.VISIBLE);
        });
        resumeButton.setOnClickListener(v -> {
            pauseMenu.setVisibility(GONE);
            gameView.setPaused(false);
        });

        btnSellTower.setOnClickListener(v -> {
            if (selectedTower == null) return;
            int price = selectedTower.getTowerType().getPrice();
            int refund = (int) Math.floor(price * 0.75);
            addMoney(refund);
            FrameLayout dragL = findViewById(R.id.dragLayer);
            dragL.removeView(selectedTower);
            for (int i = 0; i < dragL.getChildCount(); i++) {
                View child = dragL.getChildAt(i);
                if (child instanceof RangeView) {
                    float cx = selectedTower.getX() + selectedTower.getWidth() / 2f;
                    float cy = selectedTower.getY() + selectedTower.getHeight() / 2f;

                    float rX = child.getX() + ((RangeView) child).getRadius();
                    float rY = child.getY() + ((RangeView) child).getRadius();

                    if (Math.hypot(cx - rX, cy - rY) < 1f) {
                        dragL.removeView(child);
                        break;
                    }
                }
            }
            gameView.getShooter().removeTower(selectedTower);
            selectedTower = null;
            towerUpgradePopup.setVisibility(GONE);
            upgradeToggleButton.setVisibility(GONE);
            Toast.makeText(this, "Tower sold for " + refund, Toast.LENGTH_SHORT).show();
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
        btnRange.setOnClickListener(v -> {
            if (selectedTower == null) return;
            Towers type = selectedTower.getTowerType();
            if (!type.supports(UpgradeType.RANGE)) return;
            int lvl  = selectedTower.getUpgradeLevel(UpgradeType.RANGE);
            int cost = type.getUpgradeCost(UpgradeType.RANGE, lvl);
            if (!spendMoney(cost)) {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedTower.incrementUpgrade(UpgradeType.RANGE);
            selectedTower.setRadius(selectedTower.getRadius() + 70);
            int spdLvl  = selectedTower.getUpgradeLevel(UpgradeType.SPEED);
            long baseCd = selectedTower.getOriginalCooldown();
            selectedTower.setShotCooldown(baseCd / (spdLvl + 1));

            updateRangeOverlayFor(selectedTower);
            Toast.makeText(this,
                    "Range upgraded to lvl " + selectedTower.getUpgradeLevel(UpgradeType.RANGE),
                    Toast.LENGTH_SHORT).show();
            configurePopupFor(selectedTower);
        });


        btnDamage.setOnClickListener(v -> {
            Tower tower = selectedTower;
            if (tower == null) return;
            Towers type = tower.getTowerType();
            if (!type.supports(UpgradeType.DAMAGE)) return;
            int lvl  = tower.getUpgradeLevel(UpgradeType.DAMAGE);
            int cost = type.getUpgradeCost(UpgradeType.DAMAGE, lvl);
            if (!spendMoney(cost)) {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
                return;
            }
            tower.incrementUpgrade(UpgradeType.DAMAGE);
            Toast.makeText(this,
                    "Damage upgraded to lvl " + tower.getUpgradeLevel(UpgradeType.DAMAGE),
                    Toast.LENGTH_SHORT).show();
            configurePopupFor(tower);
            updateRangeOverlayFor(tower);
        });

        btnSpeed.setOnClickListener(v -> {
            Tower tower = selectedTower;
            if (tower == null) return;
            Towers type = tower.getTowerType();
            if (!type.supports(UpgradeType.SPEED)) return;

            int lvl  = tower.getUpgradeLevel(UpgradeType.SPEED);
            int cost = type.getUpgradeCost(UpgradeType.SPEED, lvl);
            if (!spendMoney(cost)) {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
                return;
            }

            tower.incrementUpgrade(UpgradeType.SPEED);

            long baseCd = tower.getOriginalCooldown();
            int  spdLvl = tower.getUpgradeLevel(UpgradeType.SPEED);
            tower.setShotCooldown(baseCd / (spdLvl + 1));

            Toast.makeText(this,
                    "Speed upgraded to lvl " + spdLvl,
                    Toast.LENGTH_SHORT).show();

            configurePopupFor(tower);
            updateRangeOverlayFor(tower);
        });

        closeButton.setOnClickListener(v -> {
            if (selectedTower != null) {
                hideRangeOverlayFor(selectedTower);
                selectedTower = null;
            }
            towerUpgradePopup.setVisibility(View.GONE);
            upgradeToggleButton.setVisibility(View.GONE);
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .build();
        } else {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }
        popSoundId = soundPool.load(this, R.raw.popc, 1);

        gameView.getShooter().setSoundPool(soundPool, popSoundId);


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
        SharedPreferences prefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);
        boolean vibrationEnabled = prefs.getBoolean("vibrationEnabled", true);
        if (vibrationEnabled) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect effect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE);
                    vibrator.vibrate(effect);
                } else {
                    vibrator.vibrate(200);
                }
            }
        }
        health -= damage;
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


    public void configurePopupFor(Tower tower) {
        Towers t = tower.getTowerType();
        if (t.supports(UpgradeType.RANGE)) {
            int lvlR  = tower.getUpgradeLevel(UpgradeType.RANGE);
            int maxR  = t.getMaxUpgradeLevel(UpgradeType.RANGE);
            if (lvlR < maxR) {
                int costR = t.getUpgradeCost(UpgradeType.RANGE, lvlR);
                btnRange.setText("Range (" + costR + ")");
                btnRange.setEnabled(true);
            } else {
                btnRange.setText("Range max");
                btnRange.setEnabled(false);
            }
            upgradeRangeContainer.setVisibility(View.VISIBLE);
        } else {
            upgradeRangeContainer.setVisibility(View.GONE);
        }
        if (t.supports(UpgradeType.DAMAGE)) {
            int lvlD  = tower.getUpgradeLevel(UpgradeType.DAMAGE);
            int maxD  = t.getMaxUpgradeLevel(UpgradeType.DAMAGE);
            if (lvlD < maxD) {
                int costD = t.getUpgradeCost(UpgradeType.DAMAGE, lvlD);
                btnDamage.setText("Damage (" + costD + ")");
                btnDamage.setEnabled(true);
            } else {
                btnDamage.setText("Damage max");
                btnDamage.setEnabled(false);
            }
            upgradeDamageContainer.setVisibility(View.VISIBLE);
        } else {
            upgradeDamageContainer.setVisibility(View.GONE);
        }
        if (t.supports(UpgradeType.SPEED)) {
            int lvlS  = tower.getUpgradeLevel(UpgradeType.SPEED);
            int maxS  = t.getMaxUpgradeLevel(UpgradeType.SPEED);
            if (lvlS < maxS) {
                int costS = t.getUpgradeCost(UpgradeType.SPEED, lvlS);
                btnSpeed.setText("Speed (" + costS + ")");
                btnSpeed.setEnabled(true);
            } else {
                btnSpeed.setText("Speed max");
                btnSpeed.setEnabled(false);
            }
            upgradeSpeedContainer.setVisibility(View.VISIBLE);
        } else {
            upgradeSpeedContainer.setVisibility(View.GONE);
        }
    }


    public void hideRangeOverlayFor(Tower tower) {
        FrameLayout dragL = findViewById(R.id.dragLayer);
        float cx = tower.getX() + tower.getWidth()/2f;
        float cy = tower.getY() + tower.getHeight()/2f;
        for (int i = 0; i < dragL.getChildCount(); i++) {
            View child = dragL.getChildAt(i);
            if (child instanceof RangeView) {
                RangeView rv = (RangeView) child;
                float r = rv.getRadius();
                float rvx = child.getX() + r;
                float rvy = child.getY() + r;
                if (Math.hypot(cx - rvx, cy - rvy) < 1f) {
                    child.setVisibility(View.INVISIBLE);
                    break;
                }
            }
        }
    }



}
