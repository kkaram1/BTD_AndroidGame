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

    private List<Bitmap> balloons;
    private ArrayList<Point> path;
    private int mapNum;
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
    public GameView(Context context,int mapNum) {
        super(context);
        this.mapNum=mapNum;
        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        int newWidth = 100;
        int newHeight = 100;
        balloons = new ArrayList<>();
        Bitmap originalRedBalloon = BitmapFactory.decodeResource(getResources(), R.drawable.red_balloon);
        balloons.add(Bitmap.createScaledBitmap(originalRedBalloon, newWidth, newHeight, true));

        Bitmap originalBlueBalloon = BitmapFactory.decodeResource(getResources(), R.drawable.blue_balloon_correct);
        balloons.add(Bitmap.createScaledBitmap(originalBlueBalloon, newWidth, newHeight, true));

        path = MapManager.getMap(mapNum).getPath();

        enemies = new ArrayList<>();

        paint.setColor(Color.TRANSPARENT);
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
            Point spawnPos = new Point(start.x, start.y);
            // TODO: 24/04/2025 change so that each phase is hardcoded can do that either with textfile or new class
            BalloonEnemy red = new BalloonEnemy(balloons.get(0), 5f, 1, spawnPos);
            BalloonEnemy blue = new BalloonEnemy(balloons.get(1), 7f, 2, spawnPos);
            if (spawnCount < phaseBalloonCount / 2) {
                enemies.add(red);
            } else {
                enemies.add(blue);
            }
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
            Bitmap balloonImage = enemy.balloonImage;
            float balloonCenterX = enemy.position.x * scaleX - balloonImage.getWidth() / 2;
            float balloonCenterY = enemy.position.y * scaleY - balloonImage.getHeight() / 2;
            canvas.drawBitmap(balloonImage, balloonCenterX, balloonCenterY, null);
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
            float dx = target.x - enemy.position.x;
            float dy = target.y - enemy.position.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < enemy.speed) {
                enemy.position.x = target.x;
                enemy.position.y = target.y;
                enemy.currentWaypointIndex++;
            } else {
                enemy.position.x += enemy.speed * (dx / distance);
                enemy.position.y += enemy.speed * (dy / distance);
            }
        }
    }
}