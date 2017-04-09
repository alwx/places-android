package me.alwx.places.utils;

import me.alwx.places.data.models.inner.Page;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * This class allows both activities and fragments to subscribe on navigation changes
 * @author alwx
 * @version 1.0
 */
public class PageNavigator {
    private PublishSubject<Page> subject = PublishSubject.create();

    /**
     * Pass a {@link Page} down to event listeners.
     *
     * @param page current page
     */
    public void navigatedTo(Page page) {
        subject.onNext(page);
    }

    /**
     * Returns an {@link Observable}
     *
     * @return PublishSubject instance
     */
    public Observable<Page> getEvents() {
        return subject;
    }
}
