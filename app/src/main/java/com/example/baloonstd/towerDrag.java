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
    private Tower currentDraggingTower;
    private final List<Tower> placedTowers = new ArrayList<>();
    private View rangeOverlay;
    private boolean draggingStarted = false;


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
                    createDraggingMonkey(view, ev);
                    draggingStarted = false;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (currentDraggingTower != null) {
                        float newX = ev.getRawX() + dragOffsetX;
                        float newY = ev.getRawY() + dragOffsetY;
                        if (!draggingStarted) {
                            if (Math.abs(newX - currentDraggingTower.getX()) > 8 || Math.abs(newY - currentDraggingTower.getY()) > 8) {
                                currentDraggingTower.setVisibility(View.VISIBLE);
                                draggingStarted = true;
                                gameView.showPathOverlay(true);
                                towerPanel.setVisibility(View.GONE);
                            }
                        }
                        if (draggingStarted) {
                            currentDraggingTower.setX(newX);
                            currentDraggingTower.setY(newY);
                        }
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    finalizeMonkeyPlacement(ev);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void createDraggingMonkey(View view, MotionEvent ev) {
        if (currentDraggingTower == null) {
            currentDraggingTower = new Tower(view.getContext());
            int sizePx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 64, view.getResources().getDisplayMetrics());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(sizePx, sizePx);
            currentDraggingTower.setLayoutParams(lp);
            currentDraggingTower.setVisibility(View.INVISIBLE);
            gameContainer.addView(currentDraggingTower);
        }
        int sizePx = currentDraggingTower.getWidth() > 0 ? currentDraggingTower.getWidth() : (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 64, view.getResources().getDisplayMetrics());
        float startX = ev.getRawX(), startY = ev.getRawY();
        currentDraggingTower.setX(startX - sizePx / 2f);
        currentDraggingTower.setY(startY - sizePx / 2f);
        dragOffsetX = currentDraggingTower.getX() - startX;
        dragOffsetY = currentDraggingTower.getY() - startY;
        currentDraggingTower.bringToFront();
    }

    private void finalizeMonkeyPlacement(MotionEvent ev) {
        if (currentDraggingTower != null && draggingStarted) {
            gameView.showPathOverlay(false);
            float cx = currentDraggingTower.getX() + currentDraggingTower.getWidth() / 2f;
            float cy = currentDraggingTower.getY() + currentDraggingTower.getHeight() / 2f;
            boolean onPath = gameView.isOnPath(cx, cy);
            Rect currRect = currentDraggingTower.getBounds();
            boolean overlap = false;
            int shrink = 70;
            currRect.inset(shrink, shrink);
            for (Tower m : placedTowers) {
                Rect r = m.getBounds();
                r.inset(shrink, shrink);
                if (Rect.intersects(currRect, m.getBounds())) {
                    overlap = true;
                    break;
                }
            }
            if (onPath || overlap) {
                currentDraggingTower.setVisibility(View.GONE);
            } else {
                currentDraggingTower.setVisibility(View.VISIBLE);
                currentDraggingTower.performClick();
                placedTowers.add(currentDraggingTower);
                currentDraggingTower.setOnClickListener(v -> {
                    if (rangeOverlay != null) {
                        gameContainer.removeView(rangeOverlay);
                        rangeOverlay = null;
                    } else {
                        int radius = 400;
                        RangeView rv = new RangeView(v.getContext(), radius);
                        FrameLayout.LayoutParams rlp = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        rv.setLayoutParams(rlp);
                        rv.measure(
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        );
                        int w = rv.getMeasuredWidth(), h = rv.getMeasuredHeight();
                        float cxx = v.getX() + v.getWidth()/2f - w/2f;
                        float cyy = v.getY() + v.getHeight()/2f - h/2f;
                        rv.setX(cxx);
                        rv.setY(cyy);
                        gameContainer.addView(rv);
                        rangeOverlay = rv;
                    }
                });

                currentDraggingTower = null;
            }
        } else {
            if (currentDraggingTower != null) {
                currentDraggingTower.setVisibility(View.GONE);
            }
        }
        towerPanel.setVisibility(View.VISIBLE);
        draggingStarted = false;
    }
}
