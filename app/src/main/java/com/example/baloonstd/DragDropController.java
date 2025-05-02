package com.example.baloonstd;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Rect;
import android.util.Log;
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
    private static final String SNIPER_TAG = "SNIPER_MONKEY";
    private final FrameLayout dragLayer;
    private final LinearLayout towerPanel;
    private final ImageView towerMonkeyIcon;
    private final ImageView towerSniperIcon;
    private final gameActivity activity;
    private final GameView gameView;
    private final List<Tower> placedTowers = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    public DragDropController(FrameLayout dragLayer,
                              LinearLayout towerPanel,
                              ImageView towerMonkeyIcon,
                              ImageView towerSniperIcon,
                              gameActivity activity) {
        this.dragLayer        = dragLayer;
        this.towerPanel       = towerPanel;
        this.towerMonkeyIcon  = towerMonkeyIcon;
        this.towerSniperIcon  = towerSniperIcon;
        this.activity         = activity;
        this.gameView         = activity.getGameView();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        towerMonkeyIcon.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = new ClipData(
                        MONKEY_TAG,
                        new String[]{ ClipDescription.MIMETYPE_TEXT_PLAIN },
                        new ClipData.Item(MONKEY_TAG)
                );
                v.startDragAndDrop(data, new View.DragShadowBuilder(v), null, 0);
                return true;
            }
            return false;
        });

        towerSniperIcon.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = new ClipData(
                        SNIPER_TAG,
                        new String[]{ ClipDescription.MIMETYPE_TEXT_PLAIN },
                        new ClipData.Item(SNIPER_TAG)
                );
                v.startDragAndDrop(data, new View.DragShadowBuilder(v), null, 0);
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
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DROP:
                    String label = event.getClipDescription().getLabel().toString();
                    Towers type = Towers.fromTag(label);
                    int cost = (type == Towers.DART_MONKEY) ? 50 : 100;

                    if (!activity.spendMoney(cost)) {
                        towerPanel.setVisibility(View.VISIBLE);
                        gameView.showPathOverlay(false);
                        return true;
                    }

                    int w = towerMonkeyIcon.getWidth();
                    int h = towerMonkeyIcon.getHeight();
                    float x = event.getX() - w/2f;
                    float y = event.getY() - h/2f;

                    Tower placed = new Tower(dragLayer.getContext(), type);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
                    placed.setLayoutParams(lp);
                    placed.setX(x);
                    placed.setY(y);

                    dragLayer.addView(placed);
                    placedTowers.add(placed);
                    gameView.registerTower(placed);
                    Log.d("DDC", "Bought " + type + " for " + cost + " at ("+x+","+y+")");

                    towerPanel.setVisibility(View.VISIBLE);
                    gameView.showPathOverlay(false);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    towerPanel.setVisibility(View.VISIBLE);
                    gameView.showPathOverlay(false);
                    return true;

                default:
                    return false;
            }
        });
    }
}
