package com.god.kahit.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum that represents the different categories that a questions can have, each enum values has a
 * to string method that returns a correct string representation of the values than can be used when
 * loading resources such as values form a database or images
 *
 * @author Johan Ek
 */
public enum Category {
    Science {
        public String toString() {
            return "science";
        }
    }, History {
        public String toString() {
            return "history";
        }
    }, Nature {
        public String toString() {
            return "nature";
        }
    }, Test {
        public String toString() {
            return "test";
        }
    }, Mix {
        public String toString() {
            return "mix";
        }
    }, Celebrities {
        public String toString() {
            return "celebrities";
        }
    }, Gaming {
        public String toString() {
            return "gaming";
        }
    }, Geography {
        public String toString() {
            return "geography";
        }
    }, Language {
        public String toString() {
            return "language";
        }
    }, Literature {
        public String toString() {
            return "literature";
        }
    }, Movies {
        public String toString() {
            return "movies";
        }
    }, Religion {
        public String toString() {
            return "religion";
        }
    }, Sports {
        public String toString() {
            return "sports";
        }
    };

    /**
     * Method that takes a string and translates it into a category
     *
     * @param category the string that should be translated if possible
     * @return the category if the string could be translated otherwise it returns null
     */
    public static Category getCategoryByString(String category) {
        switch (category) {
            case "science":
                return Science;
            case "history":
                return History;
            case "nature":
                return Nature;
            case "mix":
                return Mix;
            case "celebrities":
                return Celebrities;
            case "gaming":
                return Gaming;
            case "geography":
                return Geography;
            case "language":
                return Language;
            case "literature":
                return Literature;
            case "movies":
                return Movies;
            case "religion":
                return Religion;
            case "sports":
                return Sports;
            case "test":
                return Test;
        }
        return null;
    }

    /**
     * Method that returns a list with all categories that have questions attached to them, meaning
     * categories except test and mix
     *
     * @return a list with all categories that a question can have
     */
    public static List<Category> getRealCategories() {
        return Arrays.asList(Science, History, Nature, Celebrities, Gaming, Geography, Language,
                Literature, Movies, Religion, Sports);
    }
}
