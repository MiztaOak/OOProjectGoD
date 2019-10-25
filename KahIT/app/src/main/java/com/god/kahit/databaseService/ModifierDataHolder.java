package com.god.kahit.databaseService;

/**
 * Class that holds the data that is common across all modifiers, the data is fetched from the
 * database.
 *
 * used by: BuffDataHolder, DebuffDataHolder
 *
 * @author Johan Ek
 */
abstract class ModifierDataHolder extends ItemDataHolder {
    private double scoreMultiplier;
    private int timeHeadstart;

    ModifierDataHolder() {
        super();
    }

    double getScoreMultiplier() {
        return scoreMultiplier;
    }

    int getTimeHeadstart() {
        return timeHeadstart;
    }
}
