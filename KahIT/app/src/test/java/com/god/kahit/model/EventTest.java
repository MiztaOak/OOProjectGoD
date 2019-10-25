package com.god.kahit.model;

import com.god.kahit.model.modelEvents.DebuffPlayerEvent;
import com.god.kahit.model.modelEvents.LotteryDrawEvent;
import com.god.kahit.model.modelEvents.QuestionEvent;
import com.god.kahit.model.modelEvents.TeamChangeEvent;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventTest {

    @Test
    public void eventGetterTest(){
        Debuff debuff = new Debuff(100,"test",1,1,false,"de1");
        DebuffPlayerEvent debuffPlayerEvent = new DebuffPlayerEvent(debuff);
        Assert.assertEquals(debuff, debuffPlayerEvent.getDebuff());

        Player player = new Player("TestPlayer","p1");
        Map<Player,Item> playerItemMap = new HashMap<>();
        playerItemMap.put(player,debuff);
        LotteryDrawEvent lotteryDrawEvent = new LotteryDrawEvent(playerItemMap);
        Assert.assertEquals(playerItemMap, lotteryDrawEvent.getWinnings());

        Question question = new Question(Category.Test,"test","test1",new ArrayList<String>(),10);
        QuestionEvent questionEvent = new QuestionEvent(question, 1);
        Assert.assertEquals(1, questionEvent.getNumOfRepeats());
        Assert.assertEquals(question, questionEvent.getQuestion());

        List<Player> players = new ArrayList<>();
        players.add(player);
        Team team = new Team(players,"Team 1","t1");
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        TeamChangeEvent teamChangeEvent = new TeamChangeEvent(teams);
        Assert.assertEquals(teams, teamChangeEvent.getTeams());
    }
}
