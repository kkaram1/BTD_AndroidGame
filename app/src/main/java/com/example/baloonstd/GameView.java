package com.example.baloonstd;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.baloonstd.Balloon.Balloon;
import com.example.baloonstd.Balloon.BalloonEnemy;
import com.example.baloonstd.Map.MapManager;
import com.example.baloonstd.Phase.Phase;
import com.example.baloonstd.Phase.PhaseManager;
import com.example.baloonstd.Player.PlayerManager;
import com.example.baloonstd.Shooting.ShootingController;
import com.example.baloonstd.Tower.Tower;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private boolean isPaused = false;


    public interface OnPhaseCompleteListener { void onPhaseComplete(int phase); }
    private OnPhaseCompleteListener phaseListener;
    public void setOnPhaseCompleteListener(OnPhaseCompleteListener l) { phaseListener = l; }

    public interface OnBalloonEscapeListener { void onBalloonEscaped(int layer); }
    private OnBalloonEscapeListener escapeListener;
    public void setOnBalloonEscapeListener(OnBalloonEscapeListener l) { escapeListener = l; }

    public interface OnLayerPopListener {
        void onLayerPopped(int layer);
    }
    private OnLayerPopListener layerPopListener;
    public void setOnLayerPopListener(OnLayerPopListener l) { layerPopListener = l; }

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
        if (isPaused) {
            postInvalidateDelayed(16);
            return;
        }
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
        for (BalloonEnemy e : enemies) {
            Bitmap b = e.getImage();
            float bmpW = b.getWidth();
            float bmpH = b.getHeight();
            float cx = e.getPosition().x * scaleX;
            float cy = e.getPosition().y * scaleY;
            float uScale = Math.min(scaleX, scaleY) * 0.3f;
            float drawW = bmpW * uScale;
            float drawH = bmpH * uScale;
            if (e.getType() == Balloon.ZEPPLIN) {
                int idx = e.getCurrentWaypointIndex();
                Point next = idx < path.size() ? path.get(idx) : path.get(path.size()-1);
                float nx = next.x * scaleX;
                float ny = next.y * scaleY;
                float dx = nx - cx;
                float dy = ny - cy;
                float angle = (float)Math.toDegrees(Math.atan2(dy, dx));
                canvas.save();
                canvas.translate(cx, cy);
                canvas.rotate(angle);
                RectF dst = new RectF(
                        -drawW/2, -drawH/2,
                        drawW/2,  drawH/2);
                canvas.drawBitmap(b, null, dst, null);
                canvas.restore();
            } else {
                RectF dst = new RectF(
                        cx - drawW/2, cy - drawH/2,
                        cx + drawW/2, cy + drawH/2);
                canvas.drawBitmap(b, null, dst, null);
            }
        }
        updateEnemyPositions(deltaSec);
        shooter.updateAndDraw(deltaSec, scaleX, scaleY, canvas);
        if (enemiesToSpawn != null
                && spawnCount >= enemiesToSpawn.size()
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
            if (e.getCurrentWaypointIndex() >= path.size()) {
                if (escapeListener != null) escapeListener.onBalloonEscaped(e.getLayer());
                it.remove();
                continue;
            }
            Point tgt = path.get(e.getCurrentWaypointIndex());
            float dx = tgt.x - e.getPosition().x, dy = tgt.y - e.getPosition().y;
            float dist = (float) Math.hypot(dx, dy);
            float move = e.getSpeedPixelsPerSecond() * deltaSec;
            if (dist <= move) {
                e.getPosition().set(tgt.x, tgt.y);
                e.incCurrentWayPointIndex(1);
            } else {
                e.getPosition().x += dx / dist * move;
                e.getPosition().y += dy / dist * move;
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
        if (e.getType() == Balloon.ZEPPLIN) {
            if (!e.applyHit()) return;

            int idx = e.getCurrentWaypointIndex();

            for (int i = 0; i < 10; i++) {
                Point spawnPoint = new Point(e.getPosition());
                BalloonEnemy green = new BalloonEnemy(
                        getContext(),
                        Balloon.GREEN,
                        spawnPoint
                );
                green.setCurrentWaypointIndex(idx);
                enemies.add(green);
            }
            enemies.remove(e);
            return;
        }
        if (e.getLayer() > 1) {
            e.downgrade(getContext());
        } else {
            enemies.remove(e);
            if (layerPopListener != null) layerPopListener.onLayerPopped(e.getLayer());
        }
    }
    public void setPaused(boolean paused) {
        isPaused = paused;
        if (paused) {
            spawnHandler.removeCallbacks(spawnRunnable);
        } else {
            if (enemiesToSpawn != null && spawnCount < enemiesToSpawn.size()) {
                spawnHandler.postDelayed(spawnRunnable, 500);
            }
            lastUpdateTime = System.currentTimeMillis();
            invalidate();
        }
    }
    public List<BalloonEnemy> getEnemies() { return enemies; }
    public void registerTower(Tower t)
    {
        PlayerManager.getInstance().getPlayer().incTowers(1);
        SharedPreferences prefs = getContext().getSharedPreferences("player_session", Context.MODE_PRIVATE);
        if(!PlayerManager.getInstance().getPlayer().isGuest()){prefs.edit().putInt("towersPlaced", PlayerManager.getInstance().getPlayer().getTowersPlaced()).apply();
            updateTowersPlaced();}
        shooter.addTower(t);
    }

    private void updateTowersPlaced() {
        String url = "https://studev.groept.be/api/a24pt301/incTowers";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                },
                error -> Toast.makeText(this.getContext(), "Volley error: (network)" + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("a", String.valueOf(PlayerManager.getInstance().getPlayer().getTowersPlaced()));
                params.put("b", PlayerManager.getInstance().getUsername());
                return params;
            }
        };
        Volley.newRequestQueue(this.getContext()).add(stringRequest);
    }

    public float getMapScaleX() { return scaleX; }
    public float getMapScaleY() { return scaleY; }
    public ShootingController getShooter() {
        return shooter;
    }
}
