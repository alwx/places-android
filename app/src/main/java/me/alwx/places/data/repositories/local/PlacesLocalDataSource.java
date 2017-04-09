package me.alwx.places.data.repositories.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import me.alwx.places.data.models.Address;
import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesLocalDataSource {
    private BriteDatabase database;
    private SQLiteOpenHelper openHelper;

    public PlacesLocalDataSource(BriteDatabase database, SQLiteOpenHelper openHelper) {
        this.database = database;
        this.openHelper = openHelper;
    }

    public Observable<List<Place>> getPlaces() {
        return database
                .createQuery(Place.TABLE_NAME, Place.selectAll())
                .mapToList(new Func1<Cursor, Place>() {
                    @Override
                    public Place call(Cursor cursor) {
                        return Place.MAPPER.map(cursor);
                    }
                });
    }

    public Observable<List<Geodata>> getGeodata() {
        return database
                .createQuery(Geodata.TABLE_NAME, Geodata.selectAll())
                .mapToList(new Func1<Cursor, Geodata>() {
                    @Override
                    public Geodata call(Cursor cursor) {
                        return Geodata.MAPPER.map(cursor);
                    }
                });
    }

    public void savePlace(@NonNull Place place) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        Place.InsertRow placeInsertRow = new Place.InsertRow(db);
        placeInsertRow.bind(place.id(), place.title(), place.description(), place.phone());
        database.executeInsert(Place.TABLE_NAME, placeInsertRow.program);

        Address address = place.address();
        if (address != null) {
            Address.InsertRow addressInsertRow = new Address.InsertRow(db);
            addressInsertRow.bind(
                    place.id(),
                    address.location(),
                    address.street(),
                    address.city(),
                    address.post_code(),
                    address.country()
            );
            database.executeInsert(Address.TABLE_NAME, addressInsertRow.program);
        }
    }

    public void saveGeodata(@NonNull Geodata geodata) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        Geodata.InsertRow geodataInsertRow = new Geodata.InsertRow(db);
        geodataInsertRow.bind(geodata.id(), geodata.latitude(), geodata.longitude());
        database.executeInsert(Geodata.TABLE_NAME, geodataInsertRow.program);
    }

    public void removeAll() {
        database.execute(Place.DELETEALL);
        database.execute(Address.DELETEALL);
        database.execute(Geodata.DELETEALL);
    }
}
