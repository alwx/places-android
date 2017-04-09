package me.alwx.places.utils;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.models.inner.Page;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * This class allows both activities and fragments to subscribe on navigation changes.
 *
 * @author alwx
 * @version 1.0
 */
public class PageInteractor {
    private PublishSubject<Place> goToMapPlace = PublishSubject.create();
    private PublishSubject<Void> placesLoaded = PublishSubject.create();

    public void placesLoaded() {
        placesLoaded.onNext(null);
    }

    public void goToMapPlace(Place place) {
        goToMapPlace.onNext(place);
    }

    /**
     * Returns an {@link Observable}
     *
     * @return PublishSubject instance
     */
    public Observable<Void> getPlacesLoadedEvents() {
        return placesLoaded;
    }

    public Observable<Place> getGoToMapPlaceEvents() {
        return goToMapPlace;
    }
}
