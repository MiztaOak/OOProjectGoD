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
        @Override
        public String toString() {
            return "science";
        }
        @Override
        public String getId() { return "c1";}
    }, History {
        @Override
        public String toString() {
            return "history";
        }
        @Override
        public String getId() { return "c2";}
    }, Nature {
        @Override
        public String toString() {
            return "nature";
        }
        @Override
        public String getId() { return "c3";}
    }, Test {
        @Override
        public String toString() {
            return "test";
        }
        @Override
        public String getId() { return "c4";}
    }, Mix {
        @Override
        public String toString() {
            return "mix";
        }
        @Override
        public String getId() { return "c5";}
    }, Celebrities {
        @Override
        public String toString() {
            return "celebrities";
        }
        @Override
        public String getId() { return "c6";}
    }, Gaming {
        @Override
        public String toString() {
            return "gaming";
        }
        @Override
        public String getId() { return "c7";}
    }, Geography {
        @Override
        public String toString() {
            return "geography";
        }
        @Override
        public String getId() { return "c8";}
    }, Language {
        @Override
        public String toString() {
            return "language";
        }
        @Override
        public String getId() { return "c9";}
    }, Literature {
        @Override
        public String toString() {
            return "literature";
        }
        @Override
        public String getId() { return "c10";}
    }, Movies {
        @Override
        public String toString() {
            return "movies";
        }
        @Override
        public String getId() { return "c11";}
    }, Religion {
        @Override
        public String toString() {
            return "religion";
        }
        @Override
        public String getId() { return "c12";}
    }, Sports {
        @Override
        public String toString() {
            return "sports";
        }
        @Override
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

    public abstract String getId();
}
