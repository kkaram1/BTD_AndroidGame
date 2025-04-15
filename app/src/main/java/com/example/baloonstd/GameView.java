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

    private Bitmap balloon;
    private List<Point> path;
    private float speed = 5f;
    private Paint paint = new Paint();
    private final int nativeWidth = 500;
    private final int nativeHeight = 322;
    private float scaleX = 1f, scaleY = 1f;

    private List<BalloonEnemy> enemies;

    private Handler spawnHandler = new Handler();
    private int spawnCount = 0;
    private int phaseBalloonCount = 0;
    private int currentPhase = 1;
    private boolean phaseCompleteNotified = false;

    public interface OnPhaseCompleteListener {
        void onPhaseComplete(int phase);
    }
    private OnPhaseCompleteListener phaseListener;
    public void setOnPhaseCompleteListener(OnPhaseCompleteListener listener) {
        this.phaseListener = listener;
    }

    public GameView(Context context) {
        super(context);
        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        Bitmap originalBalloon = BitmapFactory.decodeResource(getResources(), R.drawable.red_balloon);
        int newWidth = 100;
        int newHeight = 100;
        balloon = Bitmap.createScaledBitmap(originalBalloon, newWidth, newHeight, true);

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

        enemies = new ArrayList<>();

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setPhase(int phase, int balloonCount) {
        currentPhase = phase;
        phaseBalloonCount = balloonCount;
        spawnCount = 0;
        phaseCompleteNotified = false;
        enemies.clear();
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

    private void spawnEnemy() {
        if (!path.isEmpty()) {
            Point start = path.get(0);
            BalloonEnemy enemy = new BalloonEnemy(start.x, start.y);
            enemies.add(enemy);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        scaleX = (float) w / nativeWidth;
        scaleY = (float) h / nativeHeight;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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

        for (BalloonEnemy enemy : enemies) {
            canvas.drawBitmap(balloon, enemy.posX * scaleX, enemy.posY * scaleY, null);
        }

        updateEnemyPositions();

        if (spawnCount >= phaseBalloonCount && enemies.isEmpty() && !phaseCompleteNotified) {
            phaseCompleteNotified = true;
            if (phaseListener != null) {
                phaseListener.onPhaseComplete(currentPhase);
            }
        }

        postInvalidateDelayed(16); // ~60fps
    }

    private void updateEnemyPositions() {
        Iterator<BalloonEnemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            BalloonEnemy enemy = iterator.next();
            if (enemy.currentWaypointIndex >= path.size()) {
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
