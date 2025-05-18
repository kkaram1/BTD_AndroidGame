package com.example.baloonstd.Map;

import android.graphics.Point;

import com.example.baloonstd.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        list.add(new Map(R.drawable.btdmap1, path,Collections.emptyList()));
        ArrayList<Point> path1 = new ArrayList<>();
        path1.add(new Point(0, 173));//1
        path1.add(new Point(98, 173));//2
        path1.add(new Point(115, 102));//3
        path1.add(new Point(156, 74));//4
        path1.add(new Point(199, 102));//5
        path1.add(new Point(212, 175));//6
        path1.add(new Point(305, 175));//7
        path1.add(new Point(320, 255));//8
        path1.add(new Point(375, 283));//9
        path1.add(new Point(418, 236));//10
        path1.add(new Point(422, 178));//11
        path1.add(new Point(485, 178));//12
        path1.add(new Point(485, 178));    //12
        path1.add(new Point(422, 178));    //11
        path1.add(new Point(418, 236));    //10
        path1.add(new Point(375, 283));    //9
        path1.add(new Point(320, 255));    //8
        path1.add(new Point(305, 175));    //7
        path1.add(new Point(212, 175));    //6
        path1.add(new Point(199, 102));    //5
        path1.add(new Point(156, 74));     //4
        path1.add(new Point(115, 102));    //3
        path1.add(new Point(98, 173));     //2
        path1.add(new Point(0, 173));      //1
        List<Point> noBuildZoneMap0 = Arrays.asList(
                new Point(328, 176),
                new Point(333, 148),
                new Point(355, 130),
                new Point(377, 130),
                new Point(394, 142),
                new Point(400, 161),
                new Point(398, 207),
                new Point(381, 250),
                new Point(360, 254),
                new Point(342, 241),
                new Point(336, 218)
        );
        list.add(new Map(R.drawable.btdmap2, path1,noBuildZoneMap0));
        list.add(new Map(R.drawable.red_balloon, path, Collections.emptyList()));

        return list.get(mapNum);
    }


}
