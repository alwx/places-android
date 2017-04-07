package me.alwx.places.data.repositories.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.repositories.PlacesDataSource;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesLocalDataSource implements PlacesDataSource {
    private BriteDatabase database;
    private SQLiteOpenHelper openHelper;

    public PlacesLocalDataSource(BriteDatabase database, SQLiteOpenHelper openHelper) {
        this.database = database;
        this.openHelper = openHelper;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        return database
                .createQuery(Place.TABLE_NAME, Place.selectAll())
                .mapToList(new Func1<Cursor, Place>() {
                    @Override
                    public Place call(Cursor cursor) {
                        return Place.SELECT_ALL_MAPPER.map(cursor);
                    }
                });
    }

    @Override
    public void savePlace(@NonNull Place place) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        Place.InsertRow insertRow = new Place.InsertRow(db);
        insertRow.bind(place.title(), place.description(), place.phone());

        database.executeInsert(Place.TABLE_NAME, insertRow.program);
    }

    @Override
    public void refreshPlaces() {

    }
}
