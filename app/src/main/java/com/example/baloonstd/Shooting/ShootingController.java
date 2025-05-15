package com.example.baloonstd.Shooting;

import android.graphics.Canvas;

import com.example.baloonstd.Balloon.BalloonEnemy;
import com.example.baloonstd.GameView;
import com.example.baloonstd.R;
import com.example.baloonstd.Tower.Tower;
import com.example.baloonstd.Tower.Towers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShootingController {
    private final GameView gameView;
    private final List<Tower> towers = new ArrayList<>();
    private final List<projectile> projectiles = new ArrayList<>();
    private final Map<Tower, Long> lastShotTimes = new HashMap<>();

    public ShootingController(GameView gameView) {
        this.gameView = gameView;
    }

    public void addTower(final Tower tower) {
        towers.add(tower);
        long cooldown = tower.getShotCooldown();
        lastShotTimes.put(tower, System.currentTimeMillis() - cooldown);
        if (tower.getTowerType() == Towers.DART_MONKEY) {
            tower.setImageResource(R.drawable.angrymonkey);
        }
        tower.post(new Runnable() {
            @Override
            public void run() {
                tryToShoot();
            }
        });
    }

    public void updateAndDraw(float deltaSec,
                              float scaleX,
                              float scaleY,
                              Canvas canvas) {
        Iterator<projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            projectile p = it.next();
            if (p.update(deltaSec, scaleX, scaleY)) {
                gameView.removeEnemy(p.getTarget());
                it.remove();
            } else {
                p.draw(canvas);
            }
        }
        tryToShoot();
    }

    private void tryToShoot() {
        long now = System.currentTimeMillis();
        for (Tower tower : towers) {
            long cooldown = tower.getShotCooldown();
            long last = lastShotTimes.getOrDefault(tower, 0L);
            if (now - last < cooldown) continue;

            float tx = tower.getX() + tower.getWidth() / 2f;
            float ty = tower.getY() + tower.getHeight() / 2f;

            for (BalloonEnemy e : gameView.getEnemies()) {
                float ex = e.getPosition().x * gameView.getMapScaleX();
                float ey = e.getPosition().y * gameView.getMapScaleY();
                float dx = ex - tx, dy = ey - ty;

                if (Math.hypot(dx, dy) <= tower.getRadius()) {
                    float angle = (float)Math.toDegrees(Math.atan2(dy, dx)) + 220f;
                    tower.setPivotX(tower.getWidth() / 2f);
                    tower.setPivotY(tower.getHeight() / 2f);
                    tower.setRotation(angle);

                    if (tower.getTowerType() == Towers.DART_MONKEY) {
                        tower.setImageResource(R.drawable.angrythrown);
                        tower.postDelayed(
                                () -> tower.setImageResource(R.drawable.angrymonkey),
                                500
                        );
                    }

                    // **Hier** geven we nu het projectile-resource mee:
                    int projRes = tower.getTowerType().getProjectileResId();
                    projectiles.add(new projectile(
                            gameView.getContext(),   // <-- hier de Context
                            tx,
                            ty,
                            e,
                            tower.getTowerType().getBulletSpeed(),
                            projRes
                    ));
                    lastShotTimes.put(tower, now);
                    break;
                }
            }
        }
    }

}
