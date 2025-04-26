// RangeView.java
package com.example.baloonstd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class RangeView extends View {
    private final Paint paint = new Paint();
    private int radius;

    public RangeView(Context ctx,int radius) {
        super(ctx);
        this.radius = radius;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(0xAA0000FF);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int stroke = (int) paint.getStrokeWidth();
        int diameter = radius * 2 + stroke;
        setMeasuredDimension(diameter, diameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth()/2f, getHeight()/2f, radius, paint);
    }
}
