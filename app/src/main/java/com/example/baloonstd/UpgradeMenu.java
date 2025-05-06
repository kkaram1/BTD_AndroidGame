package com.example.baloonstd;

import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.baloonstd.R;
import com.example.baloonstd.Tower;
import com.example.baloonstd.gameActivity;

public class UpgradeMenu {
    private final gameActivity activity;
    private final Tower tower;
    private final Button toggleButton;
    private final LinearLayout optionsContainer;
    private final Button incRangeButton;

    private static final int COST_RANGE = 50;
    private static final int RANGE_BOOST = 50;
    private static final long NEW_COOLDOWN_MS = 800;

    public UpgradeMenu(gameActivity activity, Tower tower) {
        this.activity = activity;
        this.tower = tower;
        toggleButton = activity.findViewById(R.id.upgradeToggleButton);
        optionsContainer = activity.findViewById(R.id.upgradeOptionsContainer);
        incRangeButton = activity.findViewById(R.id.btnIncreaseRange);
        setupListeners();
    }

    private void setupListeners() {
        toggleButton.setOnClickListener(v -> {
            boolean visible = optionsContainer.getVisibility() == View.VISIBLE;
            optionsContainer.setVisibility(visible ? View.GONE : View.VISIBLE);
        });

        incRangeButton.setOnClickListener(v -> {
            if (activity.getMoney() < COST_RANGE) {
                return;
            }
            activity.spendMoney(COST_RANGE);
            tower.setRadius(tower.getRadius() + RANGE_BOOST);
            tower.setShotCooldown(NEW_COOLDOWN_MS);
            activity.updateRangeOverlayFor(tower);
            optionsContainer.setVisibility(View.GONE);
        });
    }

    public void show() {
        float towerX = tower.getX();
        float towerY = tower.getY();
        float towerW = tower.getWidth();
        int marginDp = 4;
        float marginPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, marginDp,
                activity.getResources().getDisplayMetrics()
        );

        toggleButton.setX(towerX + towerW + marginPx);
        toggleButton.setY(towerY);
        toggleButton.bringToFront();

        optionsContainer.setX(toggleButton.getX());
        optionsContainer.setY(toggleButton.getY() + toggleButton.getHeight() + marginPx);
        optionsContainer.bringToFront();

        toggleButton.setVisibility(View.VISIBLE);
        optionsContainer.setVisibility(View.GONE);
    }
}
