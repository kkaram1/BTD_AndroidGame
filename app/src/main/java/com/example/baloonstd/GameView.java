package com.example.baloonstd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.baloonstd.Map.MapManager;
import com.example.baloonstd.Phase.Phase;
import com.example.baloonstd.Phase.PhaseManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends View {
    private final int nativeWidth = 500, nativeHeight = 322;
    private int mapNum;
    private ArrayList<Point> path;
    private float scaleX = 1f, scaleY = 1f;
    private List<BalloonEnemy> enemies = new ArrayList<>();
    private List<BalloonEnemy> enemiesToSpawn;
    private Handler spawnHandler = new Handler();
    private int spawnCount = 0;
    private boolean phaseCompleteNotified = false;
    private boolean showPathOverlay = false;
    private Point spawnPos;
    private long lastUpdateTime;
    private PhaseManager phaseManager;
    private int health = 100;

    public interface OnPhaseCompleteListener { void onPhaseComplete(int phase); }
    private OnPhaseCompleteListener phaseListener;
    public void setOnPhaseCompleteListener(OnPhaseCompleteListener l) { phaseListener = l; }

    public interface OnBalloonEscapeListener { void onBalloonEscaped(int layer); }
    private OnBalloonEscapeListener escapeListener;
    public void setOnBalloonEscapeListener(OnBalloonEscapeListener l) { escapeListener = l; }

    public interface OnBalloonPopListener { void onBalloonPop(); }
    private OnBalloonPopListener popListener;
    public void setOnBalloonPopListener(OnBalloonPopListener l) { popListener = l; }

    private ShootingController shooter;

    public GameView(Context ctx) {
        super(ctx);
        init();
    }
    public GameView(Context ctx, int mapNum) {
        super(ctx);
        this.mapNum = mapNum;
        spawnPos = MapManager.getMap(mapNum).getSpawnPoint();
        init();
    }
    public GameView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
    }
    private void init() {
        path = MapManager.getMap(mapNum).getPath();
        lastUpdateTime = System.currentTimeMillis();
        shooter = new ShootingController(this);
    }
    public void setPhase(PhaseManager pm) {
        this.phaseManager = pm;
        Phase cur = pm.getCurrentPhase();
        enemiesToSpawn = cur != null
                ? new ArrayList<>(cur.getBalloons())
                : new ArrayList<>();
        enemies.clear();
        spawnCount = 0;
        phaseCompleteNotified = false;
        spawnHandler.removeCallbacksAndMessages(null);
        spawnHandler.postDelayed(spawnRunnable, 500);
    }
    private final Runnable spawnRunnable = new Runnable() {
        @Override
        public void run() {
            if (spawnCount < enemiesToSpawn.size()) {
                BalloonEnemy e = enemiesToSpawn.get(spawnCount++);
                e.setPosition(new Point(spawnPos.x, spawnPos.y));
                enemies.add(e);
                spawnHandler.postDelayed(this, 500);
            }
        }
    };
    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        scaleX = (float) w / nativeWidth;
        scaleY = (float) h / nativeHeight;
        super.onSizeChanged(w, h, ow, oh);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        long now = System.currentTimeMillis();
        float deltaSec = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;
        super.onDraw(canvas);
        if (showPathOverlay && path.size() > 1) {
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(100f);
            Path drawPath = new Path();
            Point first = path.get(0);
            drawPath.moveTo(first.x * scaleX, first.y * scaleY);
            for (int i = 1; i < path.size(); i++) {
                Point pt = path.get(i);
                drawPath.lineTo(pt.x * scaleX, pt.y * scaleY);
            }
            canvas.drawPath(drawPath, p);
        }
        float uniformScale = Math.min(scaleX, scaleY) * 0.3f;
        for (BalloonEnemy e : enemies) {
            Bitmap b = e.getImage();
            float bmpW = b.getWidth();
            float bmpH = b.getHeight();

            float left = e.position.x * scaleX - (bmpW * uniformScale) / 2f;
            float top  = e.position.y * scaleY - (bmpH * uniformScale) / 2f;
            RectF dst = new RectF(left, top, left + bmpW * uniformScale, top  + bmpH * uniformScale);

            canvas.drawBitmap(b, null, dst, null);
        }
        updateEnemyPositions(deltaSec);
        shooter.updateAndDraw(deltaSec, scaleX, scaleY, canvas);
        if (spawnCount >= enemiesToSpawn.size()
                && enemies.isEmpty()
                && !phaseCompleteNotified) {
            phaseCompleteNotified = true;
            if (phaseListener != null) {
                phaseListener.onPhaseComplete(phaseManager.getCurrentPhaseNum());
            }
        }
        postInvalidateDelayed(16);
    }
    private void updateEnemyPositions(float deltaSec) {
        Iterator<BalloonEnemy> it = enemies.iterator();
        while (it.hasNext()) {
            BalloonEnemy e = it.next();
            if (e.currentWaypointIndex >= path.size()) {
                if (escapeListener != null) escapeListener.onBalloonEscaped(e.getLayer());
                it.remove();
                continue;
            }
            Point tgt = path.get(e.currentWaypointIndex);
            float dx = tgt.x - e.position.x, dy = tgt.y - e.position.y;
            float dist = (float) Math.hypot(dx, dy);
            float move = e.getSpeedPixelsPerSecond() * deltaSec;
            if (dist <= move) {
                e.position.set(tgt.x, tgt.y);
                e.currentWaypointIndex++;
            } else {
                e.position.x += dx / dist * move;
                e.position.y += dy / dist * move;
            }
        }
    }
    public boolean isOnPath(float viewX, float viewY) {
        float mapX = viewX / scaleX, mapY = viewY / scaleY;
        final float threshold = 26f;
        for (int i = 0; i < path.size() - 1; i++) {
            Point a = path.get(i), b = path.get(i + 1);
            if (distToSegment(mapX, mapY, a, b) < threshold) return true;
        }
        return false;
    }
    private float distToSegment(float px, float py, Point a, Point b) {
        float dx = b.x - a.x, dy = b.y - a.y;
        float len2 = dx*dx + dy*dy;
        float t = ((px - a.x)*dx + (py - a.y)*dy)/(len2==0?1:len2);
        t = Math.max(0, Math.min(1, t));
        float cx = a.x + t*dx, cy = a.y + t*dy;
        return (float)Math.hypot(px - cx, py - cy);
    }
    public void showPathOverlay(boolean show) {
        showPathOverlay = show;
        invalidate();
    }

    public void removeEnemy(BalloonEnemy e) {
        if (e.getLayer() > 1) {
            e.downgrade(getContext());
        } else {
            enemies.remove(e);
            if (popListener != null) popListener.onBalloonPop();
        }
    }

    List<BalloonEnemy> getEnemies() { return enemies; }
    public void registerTower(Tower t) { shooter.addTower(t); }
    public float getMapScaleX() { return scaleX; }
    public float getMapScaleY() { return scaleY; }
}
