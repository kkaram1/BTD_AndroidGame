package com.example.baloonstd.Shooting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.content.Context;

import com.example.baloonstd.Balloon.BalloonEnemy;

public class projectile {
    private final PointF pos;
    private final BalloonEnemy target;
    private final float speed;
    private final float radius = 16f;
    private final PointF velocity = new PointF();
    private final Bitmap bulletImage;

    public projectile(Context ctx,
                      float startX,
                      float startY,
                      BalloonEnemy target,
                      float speed,
                      int projectileResId) {
        this.pos         = new PointF(startX, startY);
        this.target      = target;
        this.speed       = speed;
        this.bulletImage = BitmapFactory.decodeResource(
                ctx.getResources(),
                projectileResId
        );
    }

    private void updateVelocity(float scaleX, float scaleY) {
        float tx = target.getPosition().x * scaleX;
        float ty = target.getPosition().y * scaleY;
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

        float tx = target.getPosition().x * scaleX;
        float ty = target.getPosition().y * scaleY;

        float half = bulletImage.getWidth() / 2f;
        return Math.hypot(pos.x - tx, pos.y - ty) < radius + half;
    }

    public void draw(Canvas canvas) {
        float halfW = bulletImage.getWidth() / 2f;
        float halfH = bulletImage.getHeight() / 2f;
        canvas.drawBitmap(bulletImage, pos.x - halfW, pos.y - halfH, null);
    }

    public BalloonEnemy getTarget() {
        return target;
    }
}
