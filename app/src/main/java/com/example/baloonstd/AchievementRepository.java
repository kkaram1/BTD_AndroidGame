package com.example.baloonstd;


import java.util.Arrays;
import java.util.List;

public class AchievementRepository {
    public static List<Achievements> getAll() {
        return Arrays.asList(
                new Achievements(
                        1,
                        "Balloon Beginner",
                        "Pop 10 balloons total",
                        player -> player.getBalloonsPopped() >= 10
                ),
                new Achievements(
                        2,
                        "Tower Tycoon",
                        "Place 50 towers",
                        player -> player.getTowersPlaced() >= 50
                ),
                 new Achievements(
                3,
                "Balloon Intermediate",
                "Pop 100 balloons",
                player -> player.getBalloonsPopped() >= 50
                 )


        );
    }
}