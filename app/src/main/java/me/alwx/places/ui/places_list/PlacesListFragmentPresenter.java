package me.alwx.places.ui.places_list;

import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.ui.Presenter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesListFragmentPresenter implements Presenter {
    private PlacesListFragment fragment;
    private PlacesRepository placesRepository;

    private boolean firstLoad = true;
    private CompositeSubscription subscriptions;

    public PlacesListFragmentPresenter(PlacesListFragment fragment,
                                       PlacesRepository placesRepository) {
        this.fragment = fragment;
        this.placesRepository = placesRepository;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void onResume() {
        fragment.initializePlaceList();
        loadPlaces(false);
    }

    @Override
    public void onPause() {
        subscriptions.clear();
    }

    @Override
    public void onDestroy() {

    }

    public void loadPlaces(boolean forceUpdate) {
        loadPlaces(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link PlacesRepository}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadPlaces(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            fragment.changeLoadingState(true);
        }
        if (forceUpdate) {
            placesRepository.refreshPlaces();
        }

        subscriptions.clear();
        Subscription subscription = placesRepository
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
                            fragment.showPlaces(places);
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
        subscriptions.add(subscription);
    }
}