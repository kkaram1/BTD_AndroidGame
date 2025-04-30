package com.example.baloonstd;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Rect;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DragDropController {
    private static final String MONKEY_TAG = "DART_MONKEY";
    private final FrameLayout dragLayer;
    private final LinearLayout towerPanel;
    private final ImageView towerMonkeyIcon;
    private final GameView gameView;
    private final List<Tower> placedTowers = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    public DragDropController(FrameLayout dragLayer,
                              LinearLayout towerPanel,
                              ImageView towerMonkeyIcon,
                              GameView gameView) {
        this.dragLayer       = dragLayer;
        this.towerPanel      = towerPanel;
        this.towerMonkeyIcon = towerMonkeyIcon;
        this.gameView        = gameView;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        towerMonkeyIcon.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData.Item item = new ClipData.Item((CharSequence)MONKEY_TAG);
                ClipData data = new ClipData(
                        MONKEY_TAG,
                        new String[]{ ClipDescription.MIMETYPE_TEXT_PLAIN },
                        item
                );
                v.startDragAndDrop(
                        data,
                        new View.DragShadowBuilder(towerMonkeyIcon),
                        null,
                        0
                );
                return true;
            }
            return false;
        });

        dragLayer.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (!event.getClipDescription()
                            .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                        return false;
                    towerPanel.setVisibility(View.GONE);
                    gameView.showPathOverlay(true);

                    // Show temporary range circle at drag position
                    int previewRange = 200;
                    RangeView previewRangeView = new RangeView(dragLayer.getContext(), previewRange);
                    FrameLayout.LayoutParams previewParams = new FrameLayout.LayoutParams(
                            previewRange * 2 + (int)previewRangeView.getPaint().getStrokeWidth(),
                            previewRange * 2 + (int)previewRangeView.getPaint().getStrokeWidth()
                    );
                    previewRangeView.setLayoutParams(previewParams);
                    previewRangeView.setX(-1000); // Off-screen initially
                    previewRangeView.setY(-1000);
                    previewRangeView.setTag("PREVIEW_RANGE");
                    dragLayer.addView(previewRangeView);

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    View previewView = dragLayer.findViewWithTag("PREVIEW_RANGE");
                    if (previewView != null) {
                        int radius = ((RangeView) previewView).getRadius();
                        float px = event.getX() - radius;
                        float py = event.getY() - radius;
                        previewView.setX(px);
                        previewView.setY(py);
                    }
                    return true;

                case DragEvent.ACTION_DROP:
                    float x = event.getX() - towerMonkeyIcon.getWidth()/2f;
                    float y = event.getY() - towerMonkeyIcon.getHeight()/2f;
                    int w = towerMonkeyIcon.getWidth();
                    int h = towerMonkeyIcon.getHeight();

                    Rect dropRect = new Rect(
                            (int)x,
                            (int)y,
                            (int)(x + w),
                            (int)(y + h)
                    );

                    int shrinkPx = 35;
                    dropRect.inset(shrinkPx, shrinkPx);

                    boolean overlap = false;
                    for (Tower m : placedTowers) {
                        Rect r2 = m.getBounds();
                        r2.inset(shrinkPx, shrinkPx);
                        if (Rect.intersects(dropRect, r2)) {
                            overlap = true;
                            break;
                        }
                    }

                    boolean onPath = gameView.isOnPath(
                            x + w/2f,
                            y + h/2f
                    );

                    if (!onPath && !overlap) {
                        Tower placed = new Tower(dragLayer.getContext());
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
                        placed.setLayoutParams(lp);
                        placed.setX(x);
                        placed.setY(y);

                        // Add range view
                        int rangeRadius = 200; // Customize this value as needed
                        RangeView rangeView = new RangeView(dragLayer.getContext(), rangeRadius);
                        FrameLayout.LayoutParams rangeParams = new FrameLayout.LayoutParams(
                                rangeRadius * 2 + (int)rangeView.getPaint().getStrokeWidth(),
                                rangeRadius * 2 + (int)rangeView.getPaint().getStrokeWidth()
                        );
                        rangeView.setLayoutParams(rangeParams);
                        rangeView.setX(x + w/2f - rangeRadius);
                        rangeView.setY(y + h/2f - rangeRadius);

                        rangeView.setVisibility(View.INVISIBLE);

                        // Add range and monkey
                        dragLayer.addView(rangeView);
                        dragLayer.addView(placed);
                        placedTowers.add(placed);

                        placed.setOnClickListener(v1 -> {
                            rangeView.setVisibility(rangeView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                        });
                    }
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    towerPanel.setVisibility(View.VISIBLE);
                    gameView.showPathOverlay(false);

                    // Remove preview range view
                    View preview = dragLayer.findViewWithTag("PREVIEW_RANGE");
                    if (preview != null) {
                        dragLayer.removeView(preview);
                    }

                    return true;

                default:
                    return false;
            }
        });
    }
}