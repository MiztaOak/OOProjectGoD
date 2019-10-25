package com.god.kahit.applicationEvents;

import com.god.kahit.model.IEvent;

/**
 * Event used to notify a observer that the current view should be switched for an other, it also
 * provides which view it should be changed to.
 *
 * used by: AfterQuestionScoreView, AfterQuestionScoreViewModel, CategoryView, CategoryViewModel,
 * PreGameCountDownView, PreGameCountDownViewModel, Repository
 *
 * @author Mats Cedervall
 */
public class NewViewEvent implements IEvent {
    private Class<?> newViewClass;

    public NewViewEvent(Class<?> newViewClass) {
        this.newViewClass = newViewClass;
    }

    public Class<?> getNewViewClass() {
        return newViewClass;
    }
}
