package me.alwx.places;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import me.alwx.places.DaggerAppComponent;
import me.alwx.places.data.components.NetworkComponent;
import me.alwx.places.data.components.DaggerNetworkComponent;
import me.alwx.places.data.modules.ApiDefaultModule;
import me.alwx.places.data.modules.ApiGoogleModule;
import me.alwx.places.data.modules.NetworkModule;
import me.alwx.places.data.PlacesComponent;
import me.alwx.places.data.PlacesModule;

/**
 * @author alwx
 * @version 1.0
 */
public class App extends Application {
    private NetworkComponent networkComponent;
    private AppComponent appComponent;
    private PlacesComponent placesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule())
                .build();

        appComponent = DaggerAppComponent.builder()
                .networkComponent(networkComponent)
                .appModule(new AppModule(this))
                .apiDefaultModule(new ApiDefaultModule())
                .apiGoogleModule(new ApiGoogleModule())
                .build();

        placesComponent = appComponent.plus(new PlacesModule());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public PlacesComponent getPlacesComponent() {
        return placesComponent;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }
}
