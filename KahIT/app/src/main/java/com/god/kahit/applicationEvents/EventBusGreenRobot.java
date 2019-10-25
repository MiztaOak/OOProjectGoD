package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;
import com.god.kahit.model.IEventBus;
/**
 * This class is responsible for posting the different
 * events to the bus for the listeners to get notified.
 *
 * used-by: This class is used in the following classes:
 * AfterQuestionScorePageView, AfterQuestionScorePageViewModel, CategoryView, CategoryViewModel
 * HotSwapAddPlayersViewModel, JoinLobbyNetView, JoinLobbyViewModel, LobbyNetView, LobbyNetViewModel
 * QuestionView, PreGameCountdownView, PreGameCountdownViewModel, QuestionView, QuestionViewModel
 * and Repository.
 *
 * @author: Mats Cedervall
 */
public class EventBusGreenRobot implements IEventBus {
    public static final org.greenrobot.eventbus.EventBus BUS = new org.greenrobot.eventbus.EventBus();

    @Override
    public void post(IEvent event) {
        BUS.post(event);
    }
}
