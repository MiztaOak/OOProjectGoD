package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

/**
 * This class is responsible for losing connection event.
 * This event is a player loses connection with the host or gets kicked from a lobby
 * so that the player goes back to the main page.
 * <p>
 * used-by: This class is used in the following classes:
 * Repository, CategoryView, AfterQuestionScorePageView, QuestionView,
 * LobbyNetView and PreGameCountdownView.
 *
 * @author: Mats Cedervall
 */
public class GameLostConnectionEvent implements IEvent {
    public GameLostConnectionEvent() {
    }
}
