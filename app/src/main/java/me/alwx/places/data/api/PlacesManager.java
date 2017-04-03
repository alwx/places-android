package me.alwx.places.data.api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import me.alwx.places.data.models.Place;
import me.alwx.places.data.modules.ApiDefaultModule;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesManager {
    private ApiDefaultModule.ApiInterface api;

    public PlacesManager(ApiDefaultModule.ApiInterface api) {
        this.api = api;
    }

    public Observable<List<Place>> getPlaces() {
        return api.getPlaces()
                .map(places -> places)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
