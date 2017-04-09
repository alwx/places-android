package me.alwx.places.data.network;

import java.util.List;

import me.alwx.places.data.models.Place;
import retrofit2.http.GET;
import rx.Observable;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public interface DefaultApiInterface {
    String SERVICE_URL = "https://alwx.me/";

    @GET("/files/places/places.json")
    Observable<List<Place>> getPlaces();
}
