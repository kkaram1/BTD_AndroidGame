package com.example.baloonstd;

import android.content.Context;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatImageView;

public class Tower extends AppCompatImageView {
    public Tower(Context context) {
        super(context);
        setImageResource(R.drawable.dartmonkey);
        setClickable(true);
    }

    public Rect getBounds() {
        int left = (int) getX();
        int top = (int) getY();
        return new Rect(left, top, left + getWidth(), top + getHeight());
    }
}