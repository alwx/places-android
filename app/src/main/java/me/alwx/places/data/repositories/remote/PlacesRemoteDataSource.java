package me.alwx.places.data.repositories.remote;

import android.support.annotation.NonNull;

import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.network.DefaultApiInterface;
import me.alwx.places.data.repositories.PlacesDataSource;
import rx.Observable;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesRemoteDataSource implements PlacesDataSource {
    private DefaultApiInterface api;

    public PlacesRemoteDataSource(DefaultApiInterface api) {
        this.api = api;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        return api.getPlaces();
    }

    @Override
    public void savePlace(@NonNull Place place) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public void refreshPlaces() {

    }
}
