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
    },
    Nature {
        public String toString() {
            return "nature";
        }
    }, Test {
        public String toString() {
            return "test";
        }
    }, Mix;

    public static Category getCategoryByIndex(int i){
        switch (i){
            case 0:
                return Science;
            case 1:
                return History;
            case 2:
                return Nature;
            case 3:
                return Mix;
        }
        return null;
    }
}
