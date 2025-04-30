package com.example.baloonstd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.baloonstd.Phase.PhaseManager;

public class gameActivity extends AppCompatActivity {
    private LinearLayout towerPanel;
    private TextView phaseAnnouncement;
    private Button nextPhaseButton;
    private GameView gameView;
    private ImageView mapImageView;
    private PhaseManager phaseManager;

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

        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        FrameLayout dragLayer = findViewById(R.id.dragLayer);
        ImageView towerMonkeyIcon = findViewById(R.id.towerMonkeyIcon);
        ImageView towerSniperIcon = findViewById(R.id.towerSniperIcon);

        Intent intent = getIntent();
        int mapNum = intent.getIntExtra("mapNum", -1);
        gameView = new GameView(this, mapNum);
        phaseManager = new PhaseManager(this);
        gameView.setPhase(phaseManager);
        gameContainer.addView(gameView);
        updateMapImage(mapNum);

        DragDropController controller = new DragDropController(
                dragLayer,
                towerPanel,
                towerMonkeyIcon,
                towerSniperIcon,
                gameView
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
    }

    private void updateMapImage(int mapNum) {
        switch (mapNum) {
            case 0: mapImageView.setImageResource(R.drawable.btdmap1); break;
            case 1: mapImageView.setImageResource(R.drawable.btdmap2); break;
            case 2: mapImageView.setImageResource(R.drawable.red_balloon); break;
        }
    }
}
