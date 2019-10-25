package com.god.kahit.model;

import com.god.kahit.EventBussMock;
import com.god.kahit.ItemDataLoaderMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayerManagerTest {
    private PlayerManager playerManagerHotswap;
    private PlayerManager playerManagerHost;
    private PlayerManager playerManagerClient;
    private List<Player> players;

    @Before
    public void setUp() {
        EventBussMock eventBussMock = new EventBussMock();
        playerManagerHotswap = new PlayerManager(eventBussMock,GameMode.HOT_SWAP);
        playerManagerHost = new PlayerManager(eventBussMock,GameMode.HOST);
        playerManagerClient = new PlayerManager(eventBussMock,GameMode.CLIENT);

        players = new ArrayList<>();
        for(int i = 1; i < 10; i++){
            players.add(new Player("Player" + i, "p"+i));
        }
    }

    @Test
    public void addNewPlayerToTeam() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        for(int i = 0; i < 8; i++){
            Assert.assertNotNull(playerManagerHotswap.getPlayerTeam(players.get(i).getId()));
        }
        Assert.assertNull(playerManagerHotswap.getPlayerTeam(players.get(8).getId()));
    }

    @Test
    public void checkAllPlayersReady() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        Assert.assertFalse(playerManagerHotswap.checkAllPlayersReady());
        for(Player player: playerManagerHotswap.getPlayers()){
            player.setReady(true);
        }
        Assert.assertTrue(playerManagerHotswap.checkAllPlayersReady());
        playerManagerHotswap.getPlayer("p1").setReady(false);
        Assert.assertFalse(playerManagerHotswap.checkAllPlayersReady());
        playerManagerHotswap.getPlayer("p1").setReady(true);
        playerManagerHotswap.resetPlayerReady();
        Assert.assertFalse(playerManagerHotswap.checkAllPlayersReady());
    }

    @Test
    public void getTotalPlayerAmountTest(){
        Assert.assertEquals(playerManagerHotswap.getTotalAmountOfPlayers(),0);
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        Assert.assertEquals(playerManagerHotswap.getTotalAmountOfPlayers(),8);
    }

    @Test
    public void applyModifiers() {
        ItemFactory.setDataLoader(new ItemDataLoaderMock());
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        List<Player> playerList = playerManagerHotswap.getPlayers();
        List<Item> itemList = ItemFactory.createStoreItems(3);
        assertNotNull(itemList);
        itemList.remove(itemList.size()-1);
        Map<Player, Item> playerItemMap = new HashMap<>();
        for(int i = 0; i < playerList.size(); i++){
            playerItemMap.put(playerList.get(i),itemList.get(i));
        }
        playerManagerHotswap.applyModifiers(playerItemMap);
        for(Player player: playerList){
            assertTrue(hasHadBuffApplied(player));
        }
    }

    private boolean hasHadBuffApplied(Player player){
        return (player.getAmountOfAlternatives() != 0 || player.isAutoAnswer() || player.getAmountOfTime() != 0
        || player.getScoreMultiplier() != 1 || player.getVanityItem() != null);
    }

    @Test
    public void incrementCurrentPlayer() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        for(int i = 0; i < 7; i++){
            Assert.assertEquals(playerManagerHotswap.getPlayers().get(i),playerManagerHotswap.getCurrentPlayer());
            playerManagerHotswap.incrementCurrentPlayer();
            Assert.assertEquals(playerManagerHotswap.getPlayers().get(i+1),playerManagerHotswap.getCurrentPlayer());
        }
        playerManagerHotswap.incrementCurrentPlayer();
        Assert.assertEquals(playerManagerHotswap.getPlayers().get(0),playerManagerHotswap.getCurrentPlayer());

    }

    @Test
    public void resetPlayerData() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        Assert.assertNotNull(playerManagerHotswap.getPlayers());
        playerManagerHotswap.resetPlayerData();
        Assert.assertNull(playerManagerHotswap.getCurrentPlayer());
        Assert.assertEquals(playerManagerHotswap.getPlayers().size(),0);
        Assert.assertEquals(playerManagerHotswap.getTeams().size(),0);
    }

    @Test
    public void changeTeam() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        Assert.assertEquals(playerManagerHotswap.getPlayerTeam("p1").getId(),"t1");
        playerManagerHotswap.changeTeam("p1","t2");
        Assert.assertEquals(playerManagerHotswap.getPlayerTeam("p1").getId(),"t2");
        Assert.assertEquals(playerManagerHotswap.getPlayerTeam("p2").getId(),"t1");
        playerManagerHotswap.changeTeam("p2","t2");
        Assert.assertEquals(playerManagerHotswap.getPlayerTeam("p2").getId(),"t2");

    }

    @Test
    public void isPlayerNameTaken(){
        playerManagerHotswap.addNewPlayerToEmptyTeam("Player 1","p1");
        playerManagerHotswap.addNewPlayerToEmptyTeam("Player 1","p2");
        assertNotEquals("Player 1", playerManagerHotswap.getPlayer("p2").getName());
        assertEquals("Player 2", playerManagerHotswap.getPlayer("p2").getName());
    }

    @Test
    public void addNewPlayerToEmptyTeam() {
        for(int i = 0; i < 100; i++){
            playerManagerClient.addNewPlayerToEmptyTeam();
        }
        Assert.assertEquals(8,playerManagerClient.getTeams().size());
    }

    @Test
    public void noEmptyTeamExists() {
        assertTrue(playerManagerClient.noEmptyTeamExists());
        for(int i = 0; i < 8; i++){
            playerManagerClient.addNewPlayerToEmptyTeam("Player" + i, "p" + i);
        }
        assertTrue(playerManagerClient.noEmptyTeamExists());
        assertEquals(playerManagerClient.getEmptyTeam(),playerManagerClient.getTeams().get(0)); //TODO check if this makes sense
        playerManagerClient.removePlayer("p1");
        assertFalse(playerManagerClient.noEmptyTeamExists());
        assertNotNull(playerManagerClient.getEmptyTeam());
        assertEquals(playerManagerClient.getEmptyTeam(),playerManagerClient.getTeams().get(1));
    }

    @Test
    public void removePlayer() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        Player p = playerManagerHotswap.getPlayer("p1");
        playerManagerHotswap.removePlayer(p);
        playerManagerHotswap.removePlayer("p2");

        assertNull(playerManagerHotswap.getPlayer(p.getId()));
        assertNull(playerManagerHotswap.getPlayer("p2"));
    }

    @Test
    public void removeTeamIfEmpty() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToEmptyTeam(p.getName(),p.getId());
        }
        Team team = playerManagerHotswap.getTeams().get(0);
        playerManagerHotswap.removePlayer("p1");
        assertEquals(0,team.getTeamMembers().size());
        playerManagerHotswap.removeTeamIfEmpty(team);
        assertFalse(playerManagerHotswap.getTeams().contains(team));
    }

    @Test
    public void changeTeamName() {
        for(Player p: players){
            playerManagerHotswap.addNewPlayerToTeam(p.getName(),p.getId(),false,"t1");
        }
        assertEquals("Team t1", playerManagerHotswap.getTeams().get(0).getTeamName());
        playerManagerHotswap.changeTeamName(playerManagerHotswap.getTeams().get(0),"CoolTeam");
        assertNotEquals("Team t1", playerManagerHotswap.getTeams().get(0).getTeamName());
        assertEquals("CoolTeam", playerManagerHotswap.getTeams().get(0).getTeamName());
    }

    @Test
    public void changePlayerName() {
        playerManagerHotswap.addNewPlayerToEmptyTeam("Player","p1");
        Player player = playerManagerHotswap.getPlayer("p1");
        assertEquals("Player",player.getName());
        player.setReady(true);
        playerManagerHotswap.changePlayerName(player,"NewName");
        assertEquals("Player",player.getName());
        player.setReady(false);
        playerManagerHotswap.changePlayerName(player,"NewName");
        assertEquals("NewName",player.getName());
    }

    @Test
    public void localPlayerIdTest(){
        assertEquals("iLocal",playerManagerHost.getLocalPlayerId());
        playerManagerHost.addNewPlayerToEmptyTeam("Player","p1");
        Player player = playerManagerHost.getPlayer("p1");
        assertNull(playerManagerHost.getCurrentPlayer());
        playerManagerHost.setLocalPlayerId("p1");
        assertEquals("p1",playerManagerHost.getLocalPlayerId());
        assertEquals(player,playerManagerHost.getCurrentPlayer());
    }
}