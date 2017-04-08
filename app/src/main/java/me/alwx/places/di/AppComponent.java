package me.alwx.places.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import me.alwx.places.di.modules.AppModule;
import me.alwx.places.di.modules.DbModule;
import me.alwx.places.di.modules.NetworkModule;
import me.alwx.places.di.modules.PlacesModule;
import me.alwx.places.utils.EventBus;

/**
 * @author alwx
 * @version 1.0
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, DbModule.class})
public interface AppComponent {
    PlacesComponent plus(PlacesModule placesModule);
}
