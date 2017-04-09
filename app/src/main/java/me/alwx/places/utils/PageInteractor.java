package me.alwx.places.utils;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.models.inner.Page;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * This class allows both activities and fragments to work with pages
 * (subscribe to navigation changes or move to some point at map)
 *
 * @author alwx
 * @version 1.0
 */
public class PageInteractor {
    private PublishSubject<Place> goToMapPlace = PublishSubject.create();
    private PublishSubject<Void> placesLoaded = PublishSubject.create();

    /**
     * Sends the event that all places has been loaded
     */
    public void placesLoaded() {
        placesLoaded.onNext(null);
    }

    /**
     * Animates to specified place on map
     *
     * @param place {@link Place} object
     */
    public void goToMapPlace(Place place) {
        goToMapPlace.onNext(place);
    }

    /**
     * @return the PublishSubject
     */
    public Observable<Void> getPlacesLoadedEvents() {
        return placesLoaded;
    }

    /**
     * @return the PublishSubject
     */
    public Observable<Place> getGoToMapPlaceEvents() {
        return goToMapPlace;
    }
}
