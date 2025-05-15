package com.example.baloonstd.Shooting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.TypedValue;

import com.example.baloonstd.Balloon.BalloonEnemy;

public class projectile {
    private final PointF pos;
    private final BalloonEnemy target;
    private final float speed;
    private final PointF velocity = new PointF();
    private final Bitmap bulletImage;
    private final float halfW, halfH;

    private final boolean isIce;
    private final int damage;

    public projectile(Context ctx,
                      float startX,
                      float startY,
                      BalloonEnemy target,
                      float speed,
                      int projectileResId,
                      int damage,
                      boolean isIce) {
        this.pos    = new PointF(startX, startY);
        this.target = target;
        this.speed  = speed;
        this.damage = damage;
        this.isIce  = isIce;

        Bitmap raw = BitmapFactory.decodeResource(ctx.getResources(), projectileResId);
        int sizePx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, ctx.getResources().getDisplayMetrics()
        );
        Bitmap scaled = Bitmap.createScaledBitmap(raw, sizePx, sizePx, true);
        raw.recycle();

        this.bulletImage = scaled;
        this.halfW = scaled.getWidth()  / 2f;
        this.halfH = scaled.getHeight() / 2f;
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
        return Math.hypot(pos.x - tx, pos.y - ty) < halfW;
    }

    public void draw(Canvas canvas) {
        float angle = (float) Math.toDegrees(Math.atan2(velocity.y, velocity.x));
        float drawAngle = angle - 90f;

        canvas.save();
        canvas.rotate(drawAngle, pos.x, pos.y);
        canvas.drawBitmap(bulletImage, pos.x - halfW, pos.y - halfH, null);
        canvas.restore();
    }

    public BalloonEnemy getTarget() {
        return target;
    }

    public boolean isIceProjectile() {
        return isIce;
    }

    public int getDamage() {
        return damage;
    }
}
