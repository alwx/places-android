package me.alwx.places.ui.presenters;

import android.location.Location;
import android.os.Bundle;

import java.util.List;

import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.ui.fragments.PlacesListFragment;
import me.alwx.places.utils.LocationUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesListFragmentPresenter {
    private PlacesListFragment fragment;
    private PlacesRepository placesRepository;
    private LocationUtils locationUtils;

    private boolean firstLoad = true;
    private CompositeSubscription loadSubscriptions = new CompositeSubscription();
    private Subscription locationSubscription;

    public PlacesListFragmentPresenter(PlacesListFragment fragment,
                                       PlacesRepository placesRepository,
                                       LocationUtils locationUtils) {
        this.fragment = fragment;
        this.placesRepository = placesRepository;
        this.locationUtils = locationUtils;
    }

    public void onCreate(Bundle state) {
        subscribeToEvents();
    }

    public void onResume() {
        fragment.initPlaceList();
        loadPlaces(false);
        locationUtils.startReceivingUpdates();
    }

    public void onPause() {
        loadSubscriptions.clear();
        locationUtils.stopReceivingUpdates();
    }

    public void onDestroy() {
        if (locationSubscription != null) {
            locationSubscription.unsubscribe();
            locationSubscription = null;
        }
    }

    public void loadPlaces(boolean forceUpdate) {
        loadPlaces(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    private void loadPlaces(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            fragment.changeLoadingState(true);
        }
        if (forceUpdate) {
            placesRepository.refreshPlaces();
        }

        loadSubscriptions.clear();

        Subscription geodataSubscription = placesRepository
                .getGeodata()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Geodata>>() {
                    @Override
                    public void call(List<Geodata> geodataList) {
                        fragment.setAdapterGeodataList(geodataList);
                    }
                });

        Subscription placesSubscription = placesRepository
                .getPlaces()
                .flatMap(new Func1<List<Place>, Observable<Place>>() {
                    @Override
                    public Observable<Place> call(List<Place> places) {
                        return Observable.from(places);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Place>>() {
                    @Override
                    public void onNext(List<Place> places) {
                        if (places.isEmpty()) {
                            fragment.showError();
                        } else {
                            fragment.showPlaceList(places);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        fragment.changeLoadingState(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fragment.showError();
                    }
                });

        loadSubscriptions.add(geodataSubscription);
        loadSubscriptions.add(placesSubscription);
    }

    private void subscribeToEvents() {
        locationSubscription = locationUtils.getLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                fragment.setAdapterLocation(location);
            }
        });
    }
}
