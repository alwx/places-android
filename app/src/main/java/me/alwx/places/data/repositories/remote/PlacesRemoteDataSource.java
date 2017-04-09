package me.alwx.places.data.repositories.remote;

import android.support.annotation.NonNull;

import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.models.gson.GeocodeResponse;
import me.alwx.places.data.network.DefaultApiInterface;
import me.alwx.places.data.network.GoogleApiInterface;
import rx.Observable;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesRemoteDataSource  {
    private DefaultApiInterface api;
    private GoogleApiInterface googleApi;

    public PlacesRemoteDataSource(DefaultApiInterface api,
                                  GoogleApiInterface googleApi) {
        this.api = api;
        this.googleApi = googleApi;
    }

    public Observable<List<Place>> getPlaces() {
        return api.getPlaces();
    }

    public Observable<GeocodeResponse> getGeoParams(String address) {
        return googleApi.getParams(address);
    }
}
