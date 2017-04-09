package me.alwx.places.di.modules;

import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.network.GoogleApiInterface;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.data.network.DefaultApiInterface;
import me.alwx.places.data.repositories.local.PlacesLocalDataSource;
import me.alwx.places.data.repositories.remote.PlacesRemoteDataSource;
import me.alwx.places.di.scopes.DataScope;

@Module
public final class PlacesModule {
    @Provides
    @DataScope
    PlacesRepository providePlacesManager(DefaultApiInterface api,
                                          GoogleApiInterface googleApi,
                                          BriteDatabase database,
                                          SQLiteOpenHelper openHelper) {

        return new PlacesRepository(
                new PlacesLocalDataSource(database, openHelper),
                new PlacesRemoteDataSource(api, googleApi)
        );
    }
}