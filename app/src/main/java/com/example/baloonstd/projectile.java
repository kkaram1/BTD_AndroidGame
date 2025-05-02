
package com.example.baloonstd;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class projectile {
    private final PointF pos;
    private final BalloonEnemy target;
    private final float speed;
    private final float radius = 16f;
    private final Paint paint = new Paint();
    private final PointF velocity = new PointF();

    public projectile(float startX, float startY, BalloonEnemy target, float speed) {
        this.pos = new PointF(startX, startY);
        this.target = target;
        this.speed = speed;
        paint.setColor(0xffffaa00);
    }

    private void updateVelocity(float scaleX, float scaleY) {
        float tx = target.position.x * scaleX;
        float ty = target.position.y * scaleY;
        float dx = tx - pos.x, dy = ty - pos.y;
        float dist = (float) Math.hypot(dx, dy);
        if (dist > 0) {
            velocity.x = dx / dist * speed;
            velocity.y = dy / dist * speed;
        }
    }

    public boolean update(float deltaSec, float scaleX, float scaleY) {
        updateVelocity(scaleX, scaleY);
        pos.x += velocity.x * deltaSec;
        pos.y += velocity.y * deltaSec;
        float tx = target.position.x * scaleX;
        float ty = target.position.y * scaleY;
        return Math.hypot(pos.x - tx, pos.y - ty) < radius + (target.balloonImage.getWidth() / 2f);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(pos.x, pos.y, radius, paint);
    }

    public BalloonEnemy getTarget() {
        return target;
    }
}
