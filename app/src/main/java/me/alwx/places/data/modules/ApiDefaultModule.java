package me.alwx.places.data.modules;

import com.google.gson.Gson;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import me.alwx.places.AppScope;
import me.alwx.places.data.models.Place;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * @author alwx
 * @version 1.0
 */
@Module
public class ApiDefaultModule {
    @Provides
    @AppScope
    ApiInterface provideDefaultApiInterface(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiInterface.SERVICE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiInterface.class);
    }

    public interface ApiInterface {
        String SERVICE_URL = "https://alwx.me/";

        @GET("/files/places/places.json")
        Observable<List<Place>> getPlaces();
    }
}
