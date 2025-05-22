package com.example.baloonstd;

import static android.view.View.GONE;
import static com.example.baloonstd.Tower.Towers.DART_MONKEY;
import static com.example.baloonstd.Tower.Towers.SNIPER_MONKEY;
import static com.example.baloonstd.Tower.Towers.ICE_MONKEY;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.baloonstd.Achievements.AchievementManager;
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
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private int money = 250;
    private int health = 50;
    private TextView healthText;
    private Button upgradeToggleButton;
    private int balloonsPopped;
    private int upgradesDone;
    private int highestRound;
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
    private TextView upgradeTitle;
    private int gamesPlayed;
    private Difficulty difficulty;
    private AchievementManager achievementManager;
    private Button        btnGeneration;
    private FrameLayout   upgradeGenerationContainer;


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
        upgradesDone = prefs.getInt("upgradesDone",0);
        highestRound = prefs.getInt("highestRound",0);
        ImageButton btnSellTower = findViewById(R.id.btnSellTower);
        btnRange  = findViewById(R.id.btnUpgradeRange);
        btnDamage = findViewById(R.id.btnUpgradeDamage);
        btnSpeed  = findViewById(R.id.btnUpgradeSpeed);
        upgradeRangeContainer  = findViewById(R.id.upgradeRangeContainer);
        upgradeDamageContainer = findViewById(R.id.upgradeDamageContainer);
        upgradeSpeedContainer  = findViewById(R.id.upgradeSpeedContainer);
        upgradeTitle = findViewById(R.id.upgradeTitle);
        btnGeneration            = findViewById(R.id.btnUpgradeGeneration);
        upgradeGenerationContainer = findViewById(R.id.upgradeGenerationContainer);



        String diffName = getIntent().getStringExtra("difficulty");
        difficulty = diffName != null ? Difficulty.valueOf(diffName) : Difficulty.MEDIUM;

        TextView dartPriceTxt = findViewById(R.id.dartPrice);
        dartPriceTxt.setText(String.valueOf(Towers.DART_MONKEY.getPrice()));

        TextView sniperPriceTxt = findViewById(R.id.sniperPrice);
        sniperPriceTxt.setText(String.valueOf(Towers.SNIPER_MONKEY.getPrice()));

        TextView icePriceTxt = findViewById(R.id.icePrice);
        icePriceTxt.setText(String.valueOf(Towers.ICE_MONKEY.getPrice()));

        TextView bankPriceTxt = findViewById(R.id.bankPrice);
        bankPriceTxt.setText(String.valueOf(Towers.BANK.getPrice()));

        gamesPlayed =prefs.getInt("gamesPlayed",0);
        if (!PlayerManager.getInstance().getPlayer().isGuest()) {saveGamesPlayed();}
        AchievementManager.init(this);
        achievementManager = AchievementManager.get();


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
        pairList.add(new Pair<>(Towers.BANK, findViewById(R.id.towerBankIcon)));


        Intent intent = getIntent();
        int mapNum = intent.getIntExtra("mapNum", -1);
        gameView = new GameView(this, mapNum);
        phaseManager = new PhaseManager(this, difficulty);
        gameContainer.addView(gameView);
        updateMapImage(mapNum);
        updateMoneyUI();

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
            updateMoneyUI();
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
            grantBankIncome();
            if (!PlayerManager.getInstance().getPlayer().isGuest()) {
                saveBalloonPop(false);
                saveUpgradesDone(false);
                if (PlayerManager.getInstance().getPlayer().setHighestRound(phase)) {
                    highestRound = phase;
                    saveHighestRound(false);
                }
            } else {
                if (PlayerManager.getInstance().getPlayer().setHighestRound(phase)) {
                    highestRound = phase;
                    saveHighestRound(true);
                }
                saveBalloonPop(true);
                saveUpgradesDone(true);
            }
            if(phase == 20){gameWonScreen.setVisibility(LinearLayout.VISIBLE);}
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
                    int earned = difficulty.getRewardPerLayer() * layer;
                    addMoney(earned);
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

            dragLayer.removeView(selectedTower);
            for (int i = 0; i < dragLayer.getChildCount(); i++) {
                View child = dragLayer.getChildAt(i);
                if (child instanceof RangeView) {
                    float cx = selectedTower.getX() + selectedTower.getWidth() / 2f;
                    float cy = selectedTower.getY() + selectedTower.getHeight() / 2f;

                    float rX = child.getX() + ((RangeView) child).getRadius();
                    float rY = child.getY() + ((RangeView) child).getRadius();

                    if (Math.hypot(cx - rX, cy - rY) < 1f) {
                        dragLayer.removeView(child);
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
            upgradesDone++;
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
            upgradesDone++;
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
            upgradesDone++;
        });
        btnGeneration.setOnClickListener(v -> {
            if (selectedTower == null) return;
            Towers tt = selectedTower.getTowerType();
            if (!tt.supports(UpgradeType.GENERATION)) return;

            int lvl  = selectedTower.getUpgradeLevel(UpgradeType.GENERATION);
            int cost = tt.getUpgradeCost(UpgradeType.GENERATION, lvl);
            if (!spendMoney(cost)) {
                Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedTower.incrementUpgrade(UpgradeType.GENERATION);

            Toast.makeText(
                    this,
                    "Bank interest +"+(lvl==0?50:75)+" / round",
                    Toast.LENGTH_SHORT
            ).show();

            configurePopupFor(selectedTower);
            upgradesDone++;
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
        updateMoneyUI();
        return true;
    }

    private void updateMoneyUI() {
        moneyText.setText("" + money);
        for (Pair<Towers, ImageView> pair : pairList) {
            if (pair.first.getPrice() > money) {
                pair.second.setAlpha(0.5f);
                pair.second.setEnabled(false);
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
        updateMoneyUI();
    }

    private void postStatToServer(String url, String a, String b) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                },
                error -> Toast.makeText(this, "Volley error: (network)" + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("a", a);
                params.put("b", b);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void updateMapImage(int mapNum) {
        ConstraintLayout.LayoutParams lp =
                (ConstraintLayout.LayoutParams) mapImageView.getLayoutParams();
        switch (mapNum) {
            case 0:
                mapImageView.setImageResource(R.drawable.btdmap1);
                mapImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mapImageView.setAdjustViewBounds(true);
                lp.dimensionRatio = "H,322:500";
                break;

            case 1:
                mapImageView.setImageResource(R.drawable.btdmap2);
                mapImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mapImageView.setAdjustViewBounds(true);
                lp.dimensionRatio = "H,322:500";
                break;

            case 2:
                mapImageView.setImageResource(R.drawable.btdmap3);
                mapImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mapImageView.setAdjustViewBounds(false);
                lp.dimensionRatio = null;
                break;
        }

        mapImageView.setLayoutParams(lp); // pas toe
    }

    private void onBalloonReachedEnd(int damage) {
        SharedPreferences prefs2 = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);
        boolean vibrationEnabled = prefs2.getBoolean("vibrationEnabled", true);
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
    private void saveHighestRound(boolean guest) {
        prefs.edit().putInt("highestRound", highestRound).apply();
        PlayerManager.getInstance().getPlayer().setHighestRound(highestRound);
        achievementManager.checkAll(PlayerManager.getInstance().getPlayer());
        if (!guest) {
            String url = "https://studev.groept.be/api/a24pt301/incHighestRound";
            postStatToServer(url, String.valueOf(highestRound), PlayerManager.getInstance().getUsername());
        }
    }

    private void saveUpgradesDone(boolean guest) {
        prefs.edit().putInt("upgradesDone", upgradesDone).apply();
        PlayerManager.getInstance().getPlayer().setTowerUpgraded(upgradesDone);
        achievementManager.checkAll(PlayerManager.getInstance().getPlayer());
        if (!guest) {
            String url = "https://studev.groept.be/api/a24pt301/incUpgradesDone";
            postStatToServer(url, String.valueOf(upgradesDone), PlayerManager.getInstance().getUsername());
        }
    }

    private void saveBalloonPop(boolean guest) {
        prefs.edit().putInt("balloonsPopped", balloonsPopped).apply();
        PlayerManager.getInstance().getPlayer().setBalloonsPopped(balloonsPopped);
        achievementManager.checkAll(PlayerManager.getInstance().getPlayer());
        if (!guest) {
            String url = "https://studev.groept.be/api/a24pt301/incBalloons";
            postStatToServer(url, String.valueOf(balloonsPopped), PlayerManager.getInstance().getUsername());
        }
    }

    private void saveGamesPlayed() {
        gamesPlayed++;
        PlayerManager.getInstance().getPlayer().setGamesPlayed(gamesPlayed);
        prefs.edit().putInt("gamesPlayed", gamesPlayed).apply();
        String url = "https://studev.groept.be/api/a24pt301/incGamesPlayed";
        postStatToServer(url, String.valueOf(gamesPlayed), PlayerManager.getInstance().getUsername());
    }


    public void configurePopupFor(Tower tower) {
        Towers t = tower.getTowerType();
        upgradeRangeContainer.setVisibility(View.GONE);
        upgradeDamageContainer.setVisibility(View.GONE);
        upgradeSpeedContainer.setVisibility(View.GONE);
        upgradeGenerationContainer.setVisibility(View.GONE);
        upgradeTitle.setText(tower.getTowerType().getDisplayName());
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
        }
        if (t.supports(UpgradeType.GENERATION)) {
            int lvl  = tower.getUpgradeLevel(UpgradeType.GENERATION);
            int max  = t.getMaxUpgradeLevel(UpgradeType.GENERATION);   // 2
            if (lvl < max) {
                int cost = t.getUpgradeCost(UpgradeType.GENERATION, lvl);
                btnGeneration.setText("Generation ("+cost+")");
                btnGeneration.setEnabled(true);
            } else {
                btnGeneration.setText("Generation max");
                btnGeneration.setEnabled(false);
            }
            upgradeGenerationContainer.setVisibility(View.VISIBLE);
        } else {
            upgradeGenerationContainer.setVisibility(View.GONE);
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
    private void grantBankIncome() {
        int gained = 0;

        FrameLayout drag = findViewById(R.id.dragLayer);
        for (int i = 0; i < drag.getChildCount(); i++) {
            View v = drag.getChildAt(i);
            if (v instanceof Tower) {
                Tower t = (Tower) v;

                if (t.getTowerType() == Towers.BANK) {
                    int lvl   = t.getUpgradeLevel(UpgradeType.GENERATION);
                    int inc   = t.getTowerType().getGenerationPerRound(lvl);
                    gained   += inc;
                }
            }
        }

        if (gained > 0) {
            addMoney(gained);
            Toast.makeText(this, "Bank income +" + gained,
                    Toast.LENGTH_SHORT).show();
        }
    }


}
