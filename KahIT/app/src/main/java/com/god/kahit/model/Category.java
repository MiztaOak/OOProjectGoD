package com.god.kahit.model;

import java.util.Arrays;
import java.util.List;

/**
 * Enum that represents the different categories that a questions can have, each enum values has a
 * to string method that returns a correct string representation of the values than can be used when
 * loading resources such as values form a database or images and an getId method that returns a unique
 * id that is used in network communication.
 *
 * @author Johan Ek
 */
public enum Category {
    Science {
        public String toString() {
            return "science";
        }
        public String getId() { return "c1";}
    }, History {
        public String toString() {
            return "history";
        }
        public String getId() { return "c2";}
    }, Nature {
        public String toString() {
            return "nature";
        }
        public String getId() { return "c3";}
    }, Test {
        public String toString() {
            return "test";
        }
        public String getId() { return "c4";}
    }, Mix {
        public String toString() {
            return "mix";
        }
        public String getId() { return "c5";}
    }, Celebrities {
        public String toString() {
            return "celebrities";
        }
        public String getId() { return "c6";}
    }, Gaming {
        public String toString() {
            return "gaming";
        }
        public String getId() { return "c7";}
    }, Geography {
        public String toString() {
            return "geography";
        }
        public String getId() { return "c8";}
    }, Language {
        public String toString() {
            return "language";
        }
        public String getId() { return "c9";}
    }, Literature {
        public String toString() {
            return "literature";
        }
        public String getId() { return "c10";}
    }, Movies {
        public String toString() {
            return "movies";
        }
        public String getId() { return "c11";}
    }, Religion {
        public String toString() {
            return "religion";
        }
        public String getId() { return "c12";}
    }, Sports {
        public String toString() {
            return "sports";
        }
        public String getId() { return "c13";}
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
