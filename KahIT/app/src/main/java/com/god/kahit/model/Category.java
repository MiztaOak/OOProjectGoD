package com.god.kahit.model;

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
    };

    public static Category getCategoryByIndex(int i) {
        switch (i) {
            case 0:
                return Science;
            case 1:
                return History;
            case 2:
                return Nature;
            case 3:
                return Mix;
            case 4:
                return Celebrities;
        }
        return null;
    }

    public static Category getCategoryByString(String category){
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
            case "test":
                return Test;
        }
        return null;
    }

    public static Category[] getRealCategories(){
        return new Category[]{Science, History, Nature, Celebrities};
    }
}
