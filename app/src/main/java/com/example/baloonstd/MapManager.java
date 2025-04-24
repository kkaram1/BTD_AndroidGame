package com.example.baloonstd;

import android.graphics.Point;
import java.util.ArrayList;

public class MapManager {
    private Map map;
    private static ArrayList<Map> list;

    public MapManager() {
    }
    public static Map getMap(int mapNum)
    {list = new ArrayList<>();
        ArrayList<Point> path = new ArrayList<>();
        path.add(new Point(0, 124));
        path.add(new Point(247, 124));
        path.add(new Point(247, 15));
        path.add(new Point(165, 15));
        path.add(new Point(165, 295));
        path.add(new Point(82, 295));
        path.add(new Point(82, 194));
        path.add(new Point(314, 194));
        path.add(new Point(314, 83));
        path.add(new Point(375, 83));
        path.add(new Point(375, 263));
        path.add(new Point(224, 263));
        path.add(new Point(224, 320));
        list.add(new Map(R.drawable.btd1_map, path));
        list.add(new Map(R.drawable.homepage_background, path));
        list.add(new Map(R.drawable.red_balloon, path));
        return list.get(mapNum);
    }


}
