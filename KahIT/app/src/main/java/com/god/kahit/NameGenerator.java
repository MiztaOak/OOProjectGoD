package com.god.kahit;

import java.util.Random;

/**
 * Utility class to generate random Android names
 */
public final class NameGenerator {
    private static final String[] COLORS =
            new String[]{
                    "Red",
//                    "Orange",
//                    "Yellow",
                    "Green",
                    "Blue",
//                    "Indigo",
//                    "Violet",
//                    "Purple",
                    "Plum",
//                    "Orchid",
//                    "Magenta",
                    "Grey",
                    "White",
            };

    private static final String[] TREATS =
            new String[]{
                    "Alpha",
                    "Beta",
                    "Cupcake",
                    "Donut",
                    "Eclair",
                    "Froyo",
                    "Ginger",
                    "Honey",
                    "Ice",
                    "Jelly",
                    "Kit Kat",
                    "Lollipop",
                    "Nougat",
                    "Oreo",
                    "Pie"
            };

    private static final String[] SPICE =
            new String[]{
                    "Ganja",
                    "Billie",
                    "Kikki",
                    "Smurf",
                    "Kapten",
                    "Swoosh",
                    "Kaffe"
            };

    private static final Random generator = new Random();

    private NameGenerator() {
    }

    /**
     * Generate a random Android agent codename
     */
    public static String generatePlayerName() {
        String spice = SPICE[generator.nextInt(SPICE.length)];
        String color = COLORS[generator.nextInt(COLORS.length)];
        return color + " " + spice;
    }

    public static String generateTeamName() {
        String treat = TREATS[generator.nextInt(TREATS.length)];
        return "Team " + treat;
    }

    public static String generateLobbyName() {
        String color = COLORS[generator.nextInt(COLORS.length)];
        return color + " Lobby";
    }
}
