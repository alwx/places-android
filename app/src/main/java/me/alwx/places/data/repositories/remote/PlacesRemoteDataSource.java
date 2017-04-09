package me.alwx.places.data.repositories.remote;

import android.support.annotation.NonNull;

import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.models.gson.GeocodeResponse;
import me.alwx.places.data.network.DefaultApiInterface;
import me.alwx.places.data.network.GoogleApiInterface;
import rx.Observable;

/**
 * Remote data source. Provides data from remote repositories.
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesRemoteDataSource {
    private DefaultApiInterface api;
    private GoogleApiInterface googleApi;

    public PlacesRemoteDataSource(@NonNull DefaultApiInterface api,
                                  @NonNull GoogleApiInterface googleApi) {
        this.api = api;
        this.googleApi = googleApi;
    }

    /**
     * Requests places from the default API.
     *
     * @return the new {@link Observable} instance
     */
    public Observable<List<Place>> getPlaces() {
        return api.getPlaces();
    }

    /**
     * Requests geo params from the Google API.
     *
     * @return the new {@link Observable} instance
     */
    public Observable<GeocodeResponse> getGeoParams(String address) {
        return googleApi.getParams(address);
    }
}
