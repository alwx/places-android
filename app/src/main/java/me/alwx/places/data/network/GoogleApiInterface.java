package me.alwx.places.data.network;

import me.alwx.places.data.models.responses.GeocodeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author alwx
 * @version 1.0
 */
public interface GoogleApiInterface {
    String SERVICE_URL = "http://maps.googleapis.com/maps/api/";

    @GET("geocode/json")
    Call<GeocodeResponse> getParams(@Query("address") String address);
}
