package me.alwx.places.data.modules;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.AppScope;
import me.alwx.places.data.models.responses.GeocodeResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author alwx
 * @version 1.0
 */
@Module
public class ApiGoogleModule {
    @Provides
    @AppScope
    ApiInterface provideGoogleApiInterface(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiInterface.SERVICE_URL)
                .client(okHttpClient)
                .build()
                .create(ApiInterface.class);
    }

    public interface ApiInterface {
        String SERVICE_URL = "http://maps.googleapis.com/maps/api/";

        @GET("geocode/json")
        Call<GeocodeResponse> getParams(@Query("address") String address);
    }
}
