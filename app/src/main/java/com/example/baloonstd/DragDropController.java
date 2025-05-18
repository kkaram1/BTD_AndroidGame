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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.baloonstd.Map.MapManager;
import com.example.baloonstd.Map.Map;
import com.example.baloonstd.Tower.Tower;
import com.example.baloonstd.Tower.Towers;

import java.util.ArrayList;
import java.util.List;

public class DragDropController {
    private final FrameLayout dragLayer;
    private final LinearLayout towerPanel;
    private final LinearLayout towerUpgradePopup;
    private final ArrayList<ImageView> towerIcons;
    private final GameView gameView;
    private final ImageButton closeButton;
    private final List<Tower> placedTowers = new ArrayList<>();
    private final GameActivity activity;
    private RangeView selectedRangeView;

    @SuppressLint("ClickableViewAccessibility")
    public DragDropController(FrameLayout dragLayer,
                              LinearLayout towerPanel,
                              List<Pair<Towers, ImageView>> towerIconList,
                              LinearLayout towerUpgradePopup,
                              ImageButton closeButton,
                              GameActivity activity) {
        this.dragLayer  = dragLayer;
        this.towerPanel = towerPanel;
        this.towerUpgradePopup = towerUpgradePopup;
        this.gameView   = activity.getGameView();
        this.closeButton = closeButton;
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
                    if (!event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                        return false;
                    towerPanel.setVisibility(View.GONE);
                    gameView.showPathOverlay(true);
                    gameView.showNoBuildOverlay(true);

                    int previewTag = Integer.parseInt(event.getClipDescription().getLabel().toString());
                    Towers previewType = getTowerByTag(previewTag);
                    RangeView previewRangeView = new RangeView(dragLayer.getContext(), previewType.getRange());
                    previewRangeView.setTag("PREVIEW_RANGE");
                    int rr = previewRangeView.getRadius();
                    float startX = event.getX() - rr;
                    float startY = event.getY() - rr;
                    previewRangeView.setX(startX);
                    previewRangeView.setY(startY);
                    dragLayer.addView(previewRangeView);
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    View pv = dragLayer.findViewWithTag("PREVIEW_RANGE");
                    if (pv instanceof RangeView) {
                        RangeView rv = (RangeView) pv;
                        int r = rv.getRadius();
                        rv.setX(event.getX() - r);
                        rv.setY(event.getY() - r);
                    }
                    return true;

                case DragEvent.ACTION_DROP:
                    int w = towerIcons.get(0).getWidth();
                    int h = towerIcons.get(0).getHeight();
                    float x = event.getX() - w/2f;
                    float y = event.getY() - h/2f;
                    Rect dropRect = new Rect(
                            (int)x, (int)y,
                            (int)(x + w), (int)(y + h)
                    );
                    int shrink = 35;
                    dropRect.inset(shrink, shrink);

                    boolean overlap = false;
                    for (Tower t : placedTowers) {
                        Rect r2 = t.getBounds();
                        r2.inset(shrink, shrink);
                        if (Rect.intersects(dropRect, r2)) {
                            overlap = true;
                            break;
                        }
                    }

                    boolean onPath = gameView.isOnPath(x + w/2f, y + h/2f);
                    int mapNum = activity.getIntent().getIntExtra("mapNum", 0);
                    Map mapObj = MapManager.getMap(mapNum);
                    float cx = x + w/2f;
                    float cy = y + h/2f;
                    int mapX = (int)(cx / gameView.getMapScaleX());
                    int mapY = (int)(cy / gameView.getMapScaleY());
                    if ( mapObj.isInNoBuildZone(mapX, mapY) ) {
                        return true;
                    }
                    if (!overlap && !onPath) {
                        int tag   = Integer.parseInt(event.getClipDescription().getLabel().toString());
                        Towers sel = getTowerByTag(tag);
                        activity.spendMoney(sel.getPrice());

                        Tower placed = new Tower(dragLayer.getContext(), sel);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
                        placed.setLayoutParams(lp);
                        placed.setX(x);
                        placed.setY(y);

                        RangeView rv = new RangeView(dragLayer.getContext(), sel.getRange());
                        FrameLayout.LayoutParams rp = new FrameLayout.LayoutParams(
                                sel.getRange()*2 + (int)rv.getPaint().getStrokeWidth(),
                                sel.getRange()*2 + (int)rv.getPaint().getStrokeWidth()
                        );
                        rv.setLayoutParams(rp);
                        rv.setX(x + w/2f - sel.getRange());
                        rv.setY(y + h/2f - sel.getRange());
                        rv.setVisibility(View.INVISIBLE);

                        dragLayer.addView(rv);
                        dragLayer.addView(placed);
                        placedTowers.add(placed);
                        gameView.registerTower(placed);
                        Log.d("DDC", "Tower placed: " + sel + " op (" + x + "," + y + ")");

                        placed.setOnClickListener(v1 -> {
                            if (activity.selectedTower == placed) {
                                rv.setVisibility(View.INVISIBLE);
                                towerUpgradePopup.setVisibility(View.GONE);
                                activity.selectedTower = null;
                                selectedRangeView     = null;
                            } else {
                                activity.selectedTower = placed;
                                rv.setVisibility(View.VISIBLE);
                                towerUpgradePopup.setVisibility(View.VISIBLE);
                                activity.configurePopupFor(placed);
                                selectedRangeView = rv;
                            }
                        });
                    }
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    towerPanel.setVisibility(View.VISIBLE);
                    gameView.showPathOverlay(false);
                    gameView.showNoBuildOverlay(false);
                    View preview = dragLayer.findViewWithTag("PREVIEW_RANGE");
                    if (preview != null) dragLayer.removeView(preview);
                    return true;

                default:
                    return false;
            }
        });

        closeButton.setOnClickListener(v -> {
            if (selectedRangeView != null) {
                selectedRangeView.setVisibility(View.GONE);
                selectedRangeView = null;
            }
            towerUpgradePopup.setVisibility(View.GONE);
            activity.selectedTower = null;
        });
    }

    private Towers getTowerByTag(int tag) {
        for (Towers t : Towers.values()) {
            if (t.getTag() == tag) return t;
        }
        throw new IllegalArgumentException("Invalid tower tag: " + tag);
    }
}
