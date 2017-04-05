package me.alwx.places.di;

import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlaceRepository;
import me.alwx.places.data.network.DefaultApiInterface;

@Module
public final class PlacesModule {
    public PlacesModule() {

    }

    @Provides
    @DataScope
    PlaceRepository providePlacesManager(DefaultApiInterface api,
                                         BriteDatabase database,
                                         SQLiteOpenHelper dbOpenHelper) {
        return new PlaceRepository(api, database, dbOpenHelper);
    }
}