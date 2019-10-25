package com.god.kahit.model;

/**
 * @responsibility: This class is responsible for posting events for the listeners.
 * used-by: This class is used in the following classes:
 * EventBusGreenRobot, Store, QuizGame and PlayerManager
 * @author: Anas Alkoutli, Johan Ek, Oussama Anadani, Jakob Ewerstrand, Mats Cedervall
 */
public interface IEventBus {
    void post(IEvent event);
}
