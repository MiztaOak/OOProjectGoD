package com.god.kahit.applicationEvents;
/**
 * @responsibility: This class is responsible for losing connection event.
 * This event is a player loses connection with the host or gets kicked from a lobby
 * so that the player goes back to the main page
 * @used-by: This class is used in the following classes:
 * Repository, CategoryView, AfterQuestionScorePageView, QuestionView,
 * LobbyNetView and PreGameCountdownView
 * @author: Anas Alkoutli
 */
import com.god.kahit.model.IEvent;

public class GameLostConnectionEvent implements IEvent {
    public GameLostConnectionEvent() {
    }
}
