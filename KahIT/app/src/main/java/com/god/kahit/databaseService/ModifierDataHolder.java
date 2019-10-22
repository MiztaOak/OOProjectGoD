package com.god.kahit.databaseService;

/**
 * Class that holds the data that is common across all modifiers, the data is fetched from the
 * database.
 *
 * used by: BuffDataHolder, DebuffDataHolder
 *
 * @author Johan Ek
 */
public abstract class ModifierDataHolder extends ItemDataHolder {
    private double scoreMultiplier;
    private int timeHeadstart;

    public ModifierDataHolder() {
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public int getTimeHeadstart() {
        return timeHeadstart;
    }
}
