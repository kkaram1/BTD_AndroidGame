package com.example.baloonstd;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class DragDropController {
    private final FrameLayout dragLayer;
    private final LinearLayout towerPanel;
    private final ArrayList<ImageView> towerIcons;
    private final GameView gameView;
    private final List<Tower> placedTowers = new ArrayList<>();
    private final gameActivity activity;

    @SuppressLint("ClickableViewAccessibility")
    public DragDropController(FrameLayout dragLayer,
                              LinearLayout towerPanel,
                              List<android.util.Pair<Towers, ImageView>> towerIconList,
                              gameActivity activity) {
        this.dragLayer  = dragLayer;
        this.towerPanel = towerPanel;
        this.gameView   = activity.getGameView();
        this.towerIcons = new ArrayList<>();
        this.activity   = activity;
        for (Pair<Towers, ImageView> pair : towerIconList) {
            Towers towerType = pair.first;
            ImageView icon   = pair.second;
            this.towerIcons.add(icon);

            icon.setOnTouchListener((v, e) -> {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    String tagString = String.valueOf(towerType.getTag());
                    ClipData.Item item = new ClipData.Item(tagString);
                    ClipData data = new ClipData(
                            tagString,
                            new String[]{ ClipDescription.MIMETYPE_TEXT_PLAIN },
                            item
                    );
                    v.startDragAndDrop(
                            data,
                            new View.DragShadowBuilder(icon),
                            null,
                            0
                    );
                    return true;
                }
                return false;
            });
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        dragLayer.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (!event.getClipDescription()
                            .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                        return false;
                    if (towerPanel != null) {
                        towerPanel.setVisibility(View.GONE);
                    }
                    gameView.showPathOverlay(true);

                    // Show temporary range circle at drag position
                    int towerTag = Integer.parseInt(event.getClipDescription().getLabel().toString());
                    Towers previewType = getTowerByTag(towerTag);
                    int previewRange = previewType.getRange();
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
                    int w, h;
                    if (event.getLocalState() instanceof View) {
                        View localView = (View) event.getLocalState();
                        w = localView.getWidth();
                        h = localView.getHeight();
                    } else {
                        w = towerIcons.isEmpty() ? 0 : towerIcons.get(0).getWidth();
                        h = towerIcons.isEmpty() ? 0 : towerIcons.get(0).getHeight();
                    }
                    float x = event.getX() - w / 2f;
                    float y = event.getY() - h / 2f;

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
                        int towerTag2 = Integer.parseInt(event.getClipDescription().getLabel().toString());
                        Towers selectedType = getTowerByTag(towerTag2);
                        activity.spendMoney(selectedType.getPrice());
                        Tower placed = new Tower(dragLayer.getContext(), selectedType);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
                        placed.setLayoutParams(lp);
                        placed.setX(x);
                        placed.setY(y);

                        // Add range view
                        int rangeRadius = selectedType.getRange();
                        RangeView rangeView = new RangeView(dragLayer.getContext(), rangeRadius);
                        FrameLayout.LayoutParams rangeParams = new FrameLayout.LayoutParams(
                                rangeRadius * 2 + (int)rangeView.getPaint().getStrokeWidth(),
                                rangeRadius * 2 + (int)rangeView.getPaint().getStrokeWidth()
                        );
                        rangeView.setLayoutParams(rangeParams);
                        rangeView.setX(x + w/2f - rangeRadius);
                        rangeView.setY(y + h/2f - rangeRadius);

                        rangeView.setVisibility(View.INVISIBLE);


                        dragLayer.addView(rangeView);
                        dragLayer.addView(placed);
                        placedTowers.add(placed);
                        Log.d("DDC", "Tower geplaatst: " + selectedType + " op (" + x + "," + y + ")");
                        gameView.registerTower(placed);

                        placed.setOnClickListener(v1 -> {

                            if (activity.selectedTower == placed) {
                                rangeView.setVisibility(View.INVISIBLE);
                                activity.upgradeOptionsContainer.setVisibility(View.GONE);
                                activity.upgradeToggleButton.setVisibility(View.GONE);
                                activity.selectedTower = null;
                            }

                            else {
                                activity.selectedTower = placed;
                                rangeView.setVisibility(View.VISIBLE);
                                new UpgradeMenu(activity, placed).show();
                            }
                        });


                    }
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    if (towerPanel != null) {
                        towerPanel.setVisibility(View.VISIBLE);
                    }
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

    private Towers getTowerByTag(int tag) {
        for (Towers t : Towers.values()) {
            if (t.getTag() == tag) return t;
        }
        throw new IllegalArgumentException("Invalid tower tag: " + tag);
    }
}
