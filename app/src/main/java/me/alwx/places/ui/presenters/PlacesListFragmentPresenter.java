package me.alwx.places.ui.presenters;

import android.location.Location;
import android.os.Bundle;

import java.util.List;

import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.ui.adapters.PlacesListAdapter;
import me.alwx.places.ui.fragments.PlacesListFragment;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PageInteractor;
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
    private PageInteractor pageInteractor;

    private boolean firstLoad = true;
    private CompositeSubscription loadSubscriptions = new CompositeSubscription();
    private Subscription locationSubscription;

    public PlacesListFragmentPresenter(Builder builder) {
        this.fragment = builder.fragment;
        this.placesRepository = builder.placesRepository;
        this.locationUtils = builder.locationUtils;
        this.pageInteractor = builder.pageInteractor;
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
                            pageInteractor.placesLoaded();
                            setOnClickListener();
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

    private void setOnClickListener() {
        fragment.setAdapterOnClickListener(
                new PlacesListAdapter.OnClickListener() {
                    @Override
                    public void onClick(Place place) {
                        pageInteractor.goToMapPlace(place);
                    }
                }
        );
    }

    public static class Builder {
        private PlacesListFragment fragment;
        private PlacesRepository placesRepository;
        private LocationUtils locationUtils;
        private PageInteractor pageInteractor;

        public Builder setFragment(PlacesListFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder setPlacesRepository(PlacesRepository repository) {
            this.placesRepository = repository;
            return this;
        }

        public Builder setLocationUtils(LocationUtils locationUtils) {
            this.locationUtils = locationUtils;
            return this;
        }

        public Builder setPageInteractor(PageInteractor pageInteractor) {
            this.pageInteractor = pageInteractor;
            return this;
        }

        public PlacesListFragmentPresenter build() {
            return new PlacesListFragmentPresenter(this);
        }
    }
}
