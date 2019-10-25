package com.god.kahit.applicationEvents;


import com.god.kahit.model.IEvent;
/**
 * This class is responsible the players state.
 * This event is fired when all players are ready for the host to start the game.
 *
 * used-by: This class is used in the following classes:
 * Repository, AfterQuestionScorePageView, CategoryView, PreGameCountdownView and QuestionView
 *
 * @author: Mats Cedervall
 */
public class AllPlayersReadyEvent implements IEvent {
    public AllPlayersReadyEvent() {
    }
}
