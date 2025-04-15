package com.example.baloonstd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends View {

    // Shared bitmap for enemy balloon
    private Bitmap balloon;
    // The path that enemies follow (native coordinates from a 500x322 design)
    private List<Point> path;
    // Movement speed (native pixels per update)
    private float speed = 5f;
    // Paint (used to draw debug path)
    private Paint paint = new Paint();

    // Native resolution (for scaling)
    private final int nativeWidth = 500;
    private final int nativeHeight = 322;
    private float scaleX = 1f, scaleY = 1f;

    // List of active enemy balloons
    private List<BalloonEnemy> enemies;

    // Handler to spawn enemies
    private Handler spawnHandler = new Handler();
    private int spawnCount = 0;
    private int phaseBalloonCount = 0;
    private int currentPhase = 1;
    private boolean phaseCompleteNotified = false;

    // Listener to notify Activity when the current phase is complete
    public interface OnPhaseCompleteListener {
        void onPhaseComplete(int phase);
    }
    private OnPhaseCompleteListener phaseListener;
    public void setOnPhaseCompleteListener(OnPhaseCompleteListener listener) {
        this.phaseListener = listener;
    }

    // Inner class representing one enemy balloon
    private class BalloonEnemy {
        float posX, posY;
        int currentWaypointIndex;
        BalloonEnemy(float startX, float startY) {
            posX = startX;
            posY = startY;
            currentWaypointIndex = 1;
        }
    }

    // Constructors
    public GameView(Context context) {
        super(context);
        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        // Load and scale the enemy balloon image (native size 100x100 for example)
        Bitmap originalBalloon = BitmapFactory.decodeResource(getResources(), R.drawable.red_balloon);
        int newWidth = 100;
        int newHeight = 100;
        balloon = Bitmap.createScaledBitmap(originalBalloon, newWidth, newHeight, true);

        // Define the path (native coordinates)
        path = new ArrayList<>();
        path.add(new Point(0, 120));
        path.add(new Point(233, 120));
        path.add(new Point(233, 30));
        path.add(new Point(145, 30));
        path.add(new Point(145, 249));
        path.add(new Point(64, 249));
        path.add(new Point(64, 177));
        path.add(new Point(295, 177));
        path.add(new Point(295, 80));
        path.add(new Point(357, 80));
        path.add(new Point(357, 226));
        path.add(new Point(203, 226));
        path.add(new Point(203, 320));

        // Initialize enemy list
        enemies = new ArrayList<>();

        // Initialize debug paint (if needed)
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    // Call this to start a phase
    public void setPhase(int phase, int balloonCount) {
        currentPhase = phase;
        phaseBalloonCount = balloonCount;
        spawnCount = 0;
        phaseCompleteNotified = false;
        enemies.clear();
        // Start spawning enemies every 500ms
        spawnHandler.removeCallbacksAndMessages(null);
        spawnHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (spawnCount < phaseBalloonCount) {
                    spawnEnemy();
                    spawnCount++;
                    spawnHandler.postDelayed(this, 500);
                }
            }
        }, 500);
    }

    // Spawn a new enemy at the start point of the path.
    private void spawnEnemy() {
        if (!path.isEmpty()) {
            Point start = path.get(0);
            BalloonEnemy enemy = new BalloonEnemy(start.x, start.y);
            enemies.add(enemy);
        }
    }

    // Calculate scaling factors when view size changes
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        scaleX = (float) w / nativeWidth;
        scaleY = (float) h / nativeHeight;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw debug path (optional)
        if (path.size() > 1) {
            Path drawPath = new Path();
            Point first = path.get(0);
            drawPath.moveTo(first.x * scaleX, first.y * scaleY);
            for (int i = 1; i < path.size(); i++) {
                Point p = path.get(i);
                drawPath.lineTo(p.x * scaleX, p.y * scaleY);
            }
            canvas.drawPath(drawPath, paint);
        }

        // Draw each enemy balloon
        for (BalloonEnemy enemy : enemies) {
            canvas.drawBitmap(balloon, enemy.posX * scaleX, enemy.posY * scaleY, null);
        }

        // Update enemy positions along the path
        updateEnemyPositions();

        // Check if phase is complete: if spawnCount reached phaseBalloonCount and no active enemies exist.
        if (spawnCount >= phaseBalloonCount && enemies.isEmpty() && !phaseCompleteNotified) {
            phaseCompleteNotified = true;
            if (phaseListener != null) {
                phaseListener.onPhaseComplete(currentPhase);
            }
        }

        postInvalidateDelayed(16); // ~60fps
    }

    // Update positions for all enemies and remove those that have reached the end.
    private void updateEnemyPositions() {
        Iterator<BalloonEnemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            BalloonEnemy enemy = iterator.next();
            if (enemy.currentWaypointIndex >= path.size()) {
                // Remove enemy if it reached the end of the path
                iterator.remove();
                continue;
            }
            Point target = path.get(enemy.currentWaypointIndex);
            float dx = target.x - enemy.posX;
            float dy = target.y - enemy.posY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < speed) {
                enemy.posX = target.x;
                enemy.posY = target.y;
                enemy.currentWaypointIndex++;
            } else {
                enemy.posX += speed * (dx / distance);
                enemy.posY += speed * (dy / distance);
            }
        }
    }
}
