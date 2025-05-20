package com.example.baloonstd.Shooting;

import android.graphics.Canvas;
import android.media.SoundPool;

import com.example.baloonstd.Balloon.Balloon;
import com.example.baloonstd.Balloon.BalloonEnemy;
import com.example.baloonstd.GameView;
import com.example.baloonstd.R;
import com.example.baloonstd.Tower.Tower;
import com.example.baloonstd.Tower.Towers;
import com.example.baloonstd.UpgradeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShootingController {
    private final GameView gameView;
    private final List<Tower> towers = new ArrayList<>();
    private final List<projectile> projectiles = new ArrayList<>();
    private final Map<Tower, Long> nextShotTime = new HashMap<>();

    private final Random random = new Random();
    private SoundPool soundPool;
    private int popSoundId;

    public ShootingController(GameView gameView) {
        this.gameView = gameView;
    }

    public void setSoundPool(SoundPool sp, int soundId) {
        this.soundPool = sp;
        this.popSoundId = soundId;
    }

    public void addTower(final Tower tower) {
        towers.add(tower);

        long now = System.currentTimeMillis();
        long baseCd = tower.getShotCooldown();

        if (tower.getTowerType() == Towers.SNIPER_MONKEY) {
            long jitter = random.nextInt((int) baseCd) + random.nextInt(600);
            nextShotTime.put(tower, now + jitter);
        } else {
            nextShotTime.put(tower, now);
        }

        if (tower.getTowerType() == Towers.DART_MONKEY) {
            tower.setImageResource(R.drawable.angrymonkey);
        }

        tower.post(this::tryToShoot);
    }

    public void updateAndDraw(float deltaSec, float scaleX, float scaleY, Canvas canvas) {
        Iterator<projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            projectile p = it.next();
            if (p.update(deltaSec, scaleX, scaleY)) {
                BalloonEnemy t = p.getTarget();
                if (p.isIceProjectile()) t.freeze(gameView.getContext());

                int dmg = p.getDamage();
                boolean popped = false;
                if (dmg > 0) {
                    if (t.getType() == Balloon.ZEPPLIN) {
                        for (int i = 0; i < dmg; i++) {
                            if (t.applyHit()) { popped = true; break; }
                        }
                    } else {
                        for (int i = 0; i < dmg - 1; i++) t.downgrade(gameView.getContext());
                        if (t.applyHit()) popped = true;
                    }
                }

                if (popped) {
                    gameView.removeEnemy(t);
                    if (soundPool != null) soundPool.play(popSoundId,1f,1f,0,0,1f);
                }
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
            long allowed = nextShotTime.getOrDefault(tower, 0L);
            if (now < allowed) continue;

            float tx = tower.getX() + tower.getWidth()/2f;
            float ty = tower.getY() + tower.getHeight()/2f;
            boolean shot = false;

            for (BalloonEnemy e : gameView.getEnemies()) {
                float ex = e.getPosition().x * gameView.getMapScaleX();
                float ey = e.getPosition().y * gameView.getMapScaleY();
                if (Math.hypot(ex-tx, ey-ty) <= tower.getRadius()) {
                    float angle = (float)Math.toDegrees(Math.atan2(ey-ty, ex-tx)) + 220f;
                    tower.setPivotX(tower.getWidth()/2f);
                    tower.setPivotY(tower.getHeight()/2f);
                    tower.setRotation(angle);

                    if (tower.getTowerType() == Towers.DART_MONKEY) {
                        tower.setImageResource(R.drawable.angrythrown);
                        tower.postDelayed(() -> tower.setImageResource(R.drawable.angrymonkey), 250);
                    } else if (tower.getTowerType() == Towers.ICE_MONKEY) {
                        tower.setImageResource(R.drawable.thrownice);
                        tower.postDelayed(() -> tower.setImageResource(R.drawable.ice_wizard), 250);
                    } else if (tower.getTowerType() == Towers.SNIPER_MONKEY) {
                        tower.setImageResource(R.drawable.thrownsnipe);
                        tower.postDelayed(() -> tower.setImageResource(R.drawable.sniper), 400);
                    }

                    int projRes    = tower.getTowerType().getProjectileResId();
                    int baseDmg    = tower.getTowerType() == Towers.ICE_MONKEY ? 0 : 1;
                    int totalDmg   = baseDmg + tower.getUpgradeLevel(UpgradeType.DAMAGE);
                    boolean isIce  = tower.getTowerType() == Towers.ICE_MONKEY;

                    projectiles.add(new projectile(
                            gameView.getContext(),
                            tx, ty,
                            e,
                            tower.getTowerType().getBulletSpeed(),
                            projRes,
                            totalDmg,
                            isIce
                    ));

                    shot = true;
                    break;
                }
            }

            if (shot) {
                long baseCd = tower.getShotCooldown();
                long nextCd = baseCd;
                if (tower.getTowerType() == Towers.SNIPER_MONKEY) {
                    // jitter ±(baseCd/2) ms
                    long jitter = random.nextInt((int)baseCd) - baseCd/4;
                    nextCd = Math.max(1, baseCd + jitter);
                }
                nextShotTime.put(tower, now + nextCd);
            }
        }
    }

    public void removeTower(Tower t) {
        towers.remove(t);
        nextShotTime.remove(t);
    }
}
