package com.example.baloonstd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class gameActivity extends AppCompatActivity {
    private LinearLayout towerPanel;
    private boolean panelVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Allow content to extend into system window areas (edge-to-edge)
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_game);

        towerPanel = findViewById(R.id.towerPanel);
        Button openPanelButton = findViewById(R.id.towerButton);
        openPanelButton.setOnClickListener(v -> togglePanel());

        FrameLayout container = findViewById(R.id.gameContainer);
        GameView gameView = new GameView(this);
        container.addView(gameView);

        // NOTE: We have removed any code that adds system insets as padding,
        // so the layout truly goes edge-to-edge.
    }

    public void togglePanel() {
        panelVisible = !panelVisible;
        towerPanel.setVisibility(panelVisible ? View.VISIBLE : View.GONE);
    }
}
