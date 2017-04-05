package me.alwx.places.di;

import javax.inject.Singleton;

import dagger.Component;
import me.alwx.places.data.db.DbModule;
import me.alwx.places.data.network.NetworkModule;

/**
 * @author alwx
 * @version 1.0
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, DbModule.class})
public interface AppComponent {
    PlacesComponent plus(PlacesModule placesModule);
}
