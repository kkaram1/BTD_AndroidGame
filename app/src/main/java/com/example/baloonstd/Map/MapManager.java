package com.example.baloonstd.Map;

import android.graphics.Point;

import com.example.baloonstd.R;

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
        list.add(new Map(R.drawable.btdmap1, path));
        ArrayList<Point> path1 = new ArrayList<>();
        path1.add(new Point(0, 166));
        path1.add(new Point(98, 166));
        path1.add(new Point(119, 97));
        path1.add(new Point(156, 77));
        path1.add(new Point(202, 97));
        path1.add(new Point(221, 162));
        path1.add(new Point(313, 167));
        path1.add(new Point(330, 242));
        path1.add(new Point(381, 270));
        path1.add(new Point(418, 225));
        path1.add(new Point(434, 170));
        path1.add(new Point(485, 170));
        path1.add(new Point(485, 170));
        path1.add(new Point(434, 170));
        path1.add(new Point(418, 225));
        path1.add(new Point(381, 250));
        path1.add(new Point(344, 242));
        path1.add(new Point(313, 167));
        path1.add(new Point(221, 162));
        path1.add(new Point(202, 97));
        path1.add(new Point(156, 77));
        path1.add(new Point(119, 97));
        path1.add(new Point(98, 166));
        path1.add(new Point(0, 166));
        list.add(new Map(R.drawable.btdmap2, path1));
        list.add(new Map(R.drawable.red_balloon, path));
        return list.get(mapNum);
    }


}
