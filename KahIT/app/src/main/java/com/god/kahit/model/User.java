package com.god.kahit.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;

    private final List<Powerups> powerups = new ArrayList<>();
    private final List<Cosmetic> cosmetics = new ArrayList<>();
}
