package com.example.baloonstd;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class towerDrag {
    private final FrameLayout gameContainer;
    private final LinearLayout towerPanel;
    private final ImageView towerMonkeyIcon;
    private final GameView gameView;
    private float dragOffsetX, dragOffsetY;
    private Monkey currentDraggingMonkey;
    private final List<Monkey> placedMonkeys = new ArrayList<>();

    public towerDrag(FrameLayout container, LinearLayout panel, ImageView icon, GameView gv) {
        this.gameContainer = container;
        this.towerPanel = panel;
        this.towerMonkeyIcon = icon;
        this.gameView = gv;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        towerMonkeyIcon.setOnTouchListener((view, ev) -> {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    gameView.showPathOverlay(true);
                    towerPanel.setVisibility(View.GONE);
                    currentDraggingMonkey = new Monkey(view.getContext());
                    int sizePx = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 64, view.getResources().getDisplayMetrics());
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(sizePx, sizePx);
                    currentDraggingMonkey.setLayoutParams(lp);
                    gameContainer.addView(currentDraggingMonkey);
                    float startX = ev.getRawX(), startY = ev.getRawY();
                    currentDraggingMonkey.setX(startX - sizePx / 2f);
                    currentDraggingMonkey.setY(startY - sizePx / 2f);
                    dragOffsetX = currentDraggingMonkey.getX() - startX;
                    dragOffsetY = currentDraggingMonkey.getY() - startY;
                    currentDraggingMonkey.bringToFront();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (currentDraggingMonkey != null) {
                        currentDraggingMonkey.setX(ev.getRawX() + dragOffsetX);
                        currentDraggingMonkey.setY(ev.getRawY() + dragOffsetY);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    gameView.showPathOverlay(false);
                    if (currentDraggingMonkey != null) {
                        float cx = currentDraggingMonkey.getX() + currentDraggingMonkey.getWidth() / 2f;
                        float cy = currentDraggingMonkey.getY() + currentDraggingMonkey.getHeight() / 2f;
                        boolean onPath = gameView.isOnPath(cx, cy);
                        Rect currRect = currentDraggingMonkey.getBounds();
                        boolean overlap = false;
                        int shrink = 70;
                        currRect.inset(shrink, shrink);
                        for (Monkey m : placedMonkeys) {
                            Rect r = m.getBounds();
                            r.inset(shrink, shrink);
                            if (Rect.intersects(currRect, m.getBounds())) {
                                overlap = true;
                                break;
                            }
                        }
                        if (onPath || overlap) {
                            gameContainer.removeView(currentDraggingMonkey);
                        } else {
                            currentDraggingMonkey.performClick();
                            placedMonkeys.add(currentDraggingMonkey);
                        }
                        currentDraggingMonkey = null;
                    }
                    towerPanel.setVisibility(View.VISIBLE);
                    return true;
                default:
                    return false;
            }
        });
    }
}
