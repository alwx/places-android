package me.alwx.places.ui.presenters;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.repositories.PlaceRepository;
import me.alwx.places.ui.adapters.PlacesAdapter;
import me.alwx.places.ui.fragments.PlacesListFragment;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesListFragmentPresenter implements Presenter {
    private PlacesListFragment fragment;
    private PlaceRepository placeRepository;

    private Subscription fetchPlacesSubscription;
    private Subscription getPlacesSubscription;

    public PlacesListFragmentPresenter(PlacesListFragment fragment,
                                       PlaceRepository placeRepository) {
        this.fragment = fragment;
        this.placeRepository = placeRepository;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        if (fetchPlacesSubscription != null) {
            fetchPlacesSubscription.unsubscribe();
        }
        if (getPlacesSubscription != null) {
            getPlacesSubscription.unsubscribe();
        }
    }

    public void getPlaces() {
        fragment.showLoading(true);

        fetchPlacesSubscription = placeRepository.fetchPlaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        getPlacesSubscription = placeRepository.getPlaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Place>>() {
                    @Override
                    public void call(List<Place> places) {
                        fragment.setPlaceList(places);
                        fragment.showLoading(false);
                    }
                });
    }
}
