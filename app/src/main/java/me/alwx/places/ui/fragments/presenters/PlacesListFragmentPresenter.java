package me.alwx.places.ui.fragments.presenters;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import me.alwx.places.data.api.PlacesManager;
import me.alwx.places.data.models.Place;
import me.alwx.places.ui.fragments.PlacesListFragment;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesListFragmentPresenter {
    private PlacesListFragment fragment;
    private PlacesManager placesManager;

    public PlacesListFragmentPresenter(PlacesListFragment fragment,
                                       PlacesManager placesManager) {
        this.fragment = fragment;
        this.placesManager = placesManager;
    }

    public void loadPlaces() {
        fragment.showLoading(true);
        placesManager.getPlaces().subscribe(new DisposableObserver<List<Place>>() {
            @Override
            public void onNext(List<Place> places) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
