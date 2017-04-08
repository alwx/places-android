package me.alwx.places.utils;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * @author alwx
 * @version 1.0
 */

public class EventBus {
    private PublishSubject<Object> subject = PublishSubject.create();

    /**
     * Pass any event down to event listeners.
     */
    public void addEvent(Object object) {
        subject.onNext(object);
    }

    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<Object> getEvents() {
        return subject;
    }
}
