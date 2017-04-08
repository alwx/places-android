package me.alwx.places;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import me.alwx.places.di.DaggerAppComponent;
import me.alwx.places.di.PlacesComponent;
import me.alwx.places.di.modules.PlacesModule;
import me.alwx.places.di.modules.DbModule;
import me.alwx.places.di.AppComponent;
import me.alwx.places.di.modules.AppModule;
import me.alwx.places.di.modules.NetworkModule;
import timber.log.Timber;

/**
 * @author alwx
 * @version 1.0
 */
public class App extends Application {
    private AppComponent appComponent;
    private PlacesComponent placesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initializeDependencyInjector();
    }

    private void initializeDependencyInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .dbModule(new DbModule(this))
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
