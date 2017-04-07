package me.alwx.places.di;

import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.data.network.DefaultApiInterface;
import me.alwx.places.data.repositories.local.PlacesLocalDataSource;
import me.alwx.places.data.repositories.remote.PlacesRemoteDataSource;

@Module
public final class PlacesModule {
    public PlacesModule() {

    }

    @Provides
    @DataScope
    PlacesRepository providePlacesManager(DefaultApiInterface api,
                                          BriteDatabase database,
                                          SQLiteOpenHelper openHelper) {

        return new PlacesRepository(
                new PlacesLocalDataSource(database, openHelper),
                new PlacesRemoteDataSource(api)
        );
    }
}