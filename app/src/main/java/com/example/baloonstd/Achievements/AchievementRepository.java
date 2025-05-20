package com.example.baloonstd.Achievements;


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
                ),
                new Achievements(
                        4,
                        "Sharp Shooter",
                        "Pop 500 balloons",
                        player -> player.getBalloonsPopped() >= 500
                ),
                new Achievements(
                        5,
                        "Popping Pro",
                        "Pop 1000 balloons",
                        player -> player.getBalloonsPopped() >= 1000
                ),
                new Achievements(
                        6,
                        "Tower Novice",
                        "Place 10 towers",
                        player -> player.getTowersPlaced() >= 10
                ),
                new Achievements(
                        7,
                        "Tower Master",
                        "Place 100 towers",
                        player -> player.getTowersPlaced() >= 100
                ),
                new Achievements(
                        8,
                        "First Upgrade",
                        "Upgrade a tower for the first time",
                        player -> player.getTowersUpgraded() >= 1
                ),
                new Achievements(
                        9,
                        "Upgrade Enthusiast",
                        "Upgrade 20 towers",
                        player -> player.getTowersUpgraded() >= 20
                ),
                new Achievements(
                        10,
                        "Round Rookie",
                        "Reach Round 10",
                        player -> player.getHighestRound() >= 10
                ),
                new Achievements(
                        11,
                        "Round Survivor",
                        "Reach Round 20",
                        player -> player.getHighestRound() >= 20
                ),
                new Achievements(
                        12,
                        "Long Haul",
                        "Reach Round 50",
                        player -> player.getHighestRound() >= 50
                ),
                new Achievements(
                        13,
                        "Gold Collector",
                        "Earn 1000 gold",
                        player -> player.getGoldEarned() >= 1000
                ),
                new Achievements(
                        14,
                        "Super Saver",
                        "Have 2000 gold at once",
                        player -> player.getCurrentGold() >= 2000
                ),
                new Achievements(
                        15,
                        "Comeback King",
                        "Survive a round with only 1 life left",
                        player -> player.getMinLifeLeftInARound() == 1
                ),
                new Achievements(
                        16,
                        "Perfect Defense",
                        "Complete a round without losing any lives",
                        player -> player.hasFlawlessRound()
                )
        );
    }
}