package com.example.baloonstd.Map;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private int mapImage;
    private ArrayList<Point> path;
    private Point spawnPoint;
    private final List<Point> noBuildPolygon;


    public Map(int mapImage,ArrayList<Point> path, List<Point> noBuildPolygon) {
        this.mapImage = mapImage;
        this.path = path;
        spawnPoint = path.get(0);
        this.noBuildPolygon = noBuildPolygon;
    }
    public List<Point> getNoBuildPolygon() { return noBuildPolygon; }
    public boolean isInNoBuildZone(int x, int y) {
        boolean inside = false;
        List<Point> poly = noBuildPolygon;
        int n = poly.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point pi = poly.get(i), pj = poly.get(j);
            if ( ((pi.y > y) != (pj.y > y)) &&
                    (x < (pj.x - pi.x) * (y - pi.y) / (float)(pj.y - pi.y) + pi.x) ) {
                inside = !inside;
            }
        }
        return inside;
    }
    public int getMapImage() {
        return mapImage;
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }
}
