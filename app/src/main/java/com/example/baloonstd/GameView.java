package com.example.baloonstd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {
    private Bitmap balloon;
    private float posX, posY;
    private List<Point> path;
    private int currentWaypointIndex = 0;
    private float speed = 5f;
    private Paint paint = new Paint();

    private final int nativeWidth = 500;
    private final int nativeHeight = 322;
    private float scaleX = 1f, scaleY = 1f;
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
        int newWidth = 200;
        int newHeight = 200;
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

        if (!path.isEmpty()){
            Point start = path.get(0);
            posX = start.x;
            posY = start.y;
            currentWaypointIndex = 1;
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

        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

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

        canvas.drawBitmap(balloon, posX * scaleX, posY * scaleY, null);

        updateEnemyPosition();
        postInvalidateDelayed(16);
    }

    private void updateEnemyPosition() {
        if (currentWaypointIndex >= path.size()) {
            return;
        }
        Point target = path.get(currentWaypointIndex);
        float dx = target.x - posX;
        float dy = target.y - posY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            posX = target.x;
            posY = target.y;
            currentWaypointIndex++;
        } else {
            posX += speed * (dx / distance);
            posY += speed * (dy / distance);
        }
    }
}

