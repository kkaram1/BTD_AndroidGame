package com.example.baloonstd;
import android.graphics.Path;
import android.graphics.Point;
import java.util.ArrayList;

public class Map {
    private int mapImage;
    private ArrayList<Point> path;
    private Point spawnPoint;

    public Map(int mapImage,ArrayList<Point> path) {
        this.mapImage = mapImage;
        this.path = path;
        spawnPoint = path.get(0);
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
