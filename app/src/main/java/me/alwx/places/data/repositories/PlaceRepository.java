package me.alwx.places.data.repositories;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.network.DefaultApiInterface;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * @author alwx
 * @version 1.0
 */

public class PlaceRepository {
    private DefaultApiInterface api;
    private BriteDatabase database;
    private SQLiteOpenHelper dbOpenHelper;

    public PlaceRepository(DefaultApiInterface api,
                           BriteDatabase database,
                           SQLiteOpenHelper dbOpenHelper) {
        this.api = api;
        this.database = database;
        this.dbOpenHelper = dbOpenHelper;
    }

    public Observable<String> fetchPlaces() {
        final BehaviorSubject<String> requestSubject = BehaviorSubject.create();

        api.getPlaces().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Place>>() {
                    @Override
                    public void onCompleted() {
                        requestSubject.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestSubject.onError(e);
                    }

                    @Override
                    public void onNext(List<Place> places) {
                        Place.deleteAll(dbOpenHelper);
                        Place.insert(dbOpenHelper, places.get(0));
                        requestSubject.onNext("");
                    }
                });

        return requestSubject.asObservable().cache();
    }

    public Observable<List<Place>> getPlaces() {
        return database
                .createQuery(Place.TABLE_NAME, Place.selectAll())
                .map(new Func1<SqlBrite.Query, List<Place>>() {
                    @Override
                    public List<Place> call(SqlBrite.Query query) {
                        List<Place> result = new ArrayList<>();
                        try (Cursor cursor = query.run()) {
                            while (cursor.moveToNext()) {
                                result.add(Place.SELECT_ALL_MAPPER.map(cursor));
                            }
                        }
                        return result;
                    }
                });
    }
}
