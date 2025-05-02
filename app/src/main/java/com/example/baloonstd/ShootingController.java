package com.example.baloonstd;

import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShootingController {
    private final GameView gameView;
    private final List<Tower> towers = new ArrayList<>();
    private final List<projectile> projectiles = new ArrayList<>();
    private long lastShotTime = 0;
    private  long SHOT_COOLDOWN_MS;

    public ShootingController(GameView gameView) {
        this.gameView = gameView;
    }

    public void addTower(final Tower tower) {
        SHOT_COOLDOWN_MS=tower.getShotCooldown();
        towers.add(tower);
        if (tower.getTowerType() == Towers.DART_MONKEY) {
            tower.setImageResource(R.drawable.angrymonkey);
        }
        tower.post(new Runnable() {
            @Override
            public void run() {
                lastShotTime = System.currentTimeMillis() - SHOT_COOLDOWN_MS;
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
        if (now - lastShotTime < SHOT_COOLDOWN_MS) return;

        for (Tower tower : towers) {
            float tx = tower.getX() + tower.getWidth()/2f;
            float ty = tower.getY() + tower.getHeight()/2f;
            for (BalloonEnemy e : gameView.getEnemies()) {
                float ex = e.position.x * gameView.getMapScaleX();
                float ey = e.position.y * gameView.getMapScaleY();
                float dx = ex - tx, dy = ey - ty;
                if (Math.hypot(dx, dy) <= tower.getRadius()) {
                    float angle = (float)Math.toDegrees(Math.atan2(dy, dx)) + 220f;
                    tower.setPivotX(tower.getWidth()/2f);
                    tower.setPivotY(tower.getHeight()/2f);
                    tower.setRotation(angle);

                    if (tower.getTowerType() == Towers.DART_MONKEY) {
                        tower.setImageResource(R.drawable.angrythrown);
                        tower.postDelayed(
                                () -> tower.setImageResource(R.drawable.angrymonkey),
                                900
                        );
                    }

                    projectiles.add(new projectile(tx, ty, e, 1000f));
                    lastShotTime = now;
                    return;
                }
            }
        }
    }
}
