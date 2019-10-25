package com.god.kahit.Repository;

import java.util.Random;

/**
 * responsibility: Utility class to generate random Android names
 * <p>
 * used-by: JoinLobbyViewModel, CreateLobbyNetViewModel.
 *
 * @author Mats Cedervall
 */
public final class NameGenerator {
    private static final String[] COLORS =
            new String[]{ //Max 5 chars
                    "Red",
                    "Green",
                    "Blue",
                    "Plum",
                    "Grey",
                    "White",
            };

    private static final String[] TREATS =
            new String[]{ //Max 6 chars
                    "Alpha",
                    "Beta",
                    "Donut",
                    "Eclair",
                    "Froyo",
                    "Ginger",
                    "Honey",
                    "Ice",
                    "Jelly",
                    "Nougat",
                    "Oreo",
                    "Pie"
            };

    private static final String[] SPICE =
            new String[]{ //Max 6 chars
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
