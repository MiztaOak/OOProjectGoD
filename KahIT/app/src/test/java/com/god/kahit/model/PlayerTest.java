package com.god.kahit.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    private Player player;

    @Before
    public void setup() {
        player = new Player("Player", "p1");
    }

    @Test
    public void getterTest() {
        Assert.assertEquals("Player", player.getName());
        Assert.assertEquals("p1", player.getId());
        player.setId("p2");
        Assert.assertEquals("p2", player.getId());
    }

    @Test
    public void updateScoreTest() {
        Assert.assertEquals(0, player.getScore());
        player.updateScore(100,100);
        Assert.assertEquals(500,player.getScore());
        player.setAmountOfTime(50);
        player.updateScore(50,100);
        Assert.assertEquals(1000,player.getScore());
        player.setAmountOfTime(50);
        player.updateScore(100,100);
        Assert.assertEquals(1500,player.getScore());
        player.clearModifier();
        Assert.assertEquals(0,player.getAmountOfTime());
        player.setScoreMultiplier(2);
        player.updateScore(100,100);
        Assert.assertEquals(2500,player.getScore());
    }

    @Test
    public void modifierTest(){
        Assert.assertEquals(1,player.getScoreMultiplier(),0.1);
        Assert.assertEquals(0,player.getAmountOfTime());
        Assert.assertFalse(player.isAutoAnswer());
        Assert.assertEquals(0,player.getAmountOfAlternatives());
        player.setScoreMultiplier(2);
        player.setAmountOfTime(50);
        player.setAutoAnswer(true);
        player.setAmountOfAlternatives(2);
        Assert.assertEquals(2,player.getScoreMultiplier(),0.1);
        Assert.assertEquals(50,player.getAmountOfTime());
        Assert.assertTrue(player.isAutoAnswer());
        Assert.assertEquals(2,player.getAmountOfAlternatives());
        player.clearModifier();
        Assert.assertEquals(1,player.getScoreMultiplier(),0.1);
        Assert.assertEquals(0,player.getAmountOfTime());
        Assert.assertFalse(player.isAutoAnswer());
        Assert.assertEquals(0,player.getAmountOfAlternatives());
    }
}
