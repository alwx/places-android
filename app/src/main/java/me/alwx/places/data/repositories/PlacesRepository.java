package me.alwx.places.data.repositories;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.network.DefaultApiInterface;
import me.alwx.places.data.repositories.local.PlacesLocalDataSource;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesRepository implements PlacesDataSource {
    private PlacesDataSource localDataSource;
    private PlacesDataSource remoteDataSource;

    private Map<Long, Place> cachedPlaces;
    private boolean cacheIsDirty = false;

    public PlacesRepository(PlacesDataSource localDataSource,
                            PlacesDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        if (cachedPlaces != null && !cacheIsDirty) {
            return Observable.from(cachedPlaces.values()).toList();
        } else if (cachedPlaces == null) {
            cachedPlaces = new LinkedHashMap<>();
        }

        Observable<List<Place>> remotePlaces = getAndSaveRemotePlaces();
        if (cacheIsDirty) {
            return remotePlaces;
        } else {
            Observable<List<Place>> localPlaces = getAndCacheLocalPlaces();
            return Observable.concat(localPlaces, remotePlaces)
                    .filter(new Func1<List<Place>, Boolean>() {
                        @Override
                        public Boolean call(List<Place> places) {
                            return !places.isEmpty();
                        }
                    })
                    .first();
        }
    }

    @Override
    public void savePlace(@NonNull Place place) {

    }

    @Override
    public void refreshPlaces() {
        cacheIsDirty = true;
    }

    private Observable<List<Place>> getAndSaveRemotePlaces() {
        return remoteDataSource
                .getPlaces()
                .flatMap(new Func1<List<Place>, Observable<List<Place>>>() {
                    @Override
                    public Observable<List<Place>> call(List<Place> places) {
                        return Observable.from(places).doOnNext(new Action1<Place>() {
                            @Override
                            public void call(Place place) {
                                localDataSource.savePlace(place);
                                cachedPlaces.put(place._id(), place);
                            }
                        }).toList();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        cacheIsDirty = false;
                    }
                });
    }

    private Observable<List<Place>> getAndCacheLocalPlaces() {
        return localDataSource
                .getPlaces()
                .flatMap(new Func1<List<Place>, Observable<List<Place>>>() {
                    @Override
                    public Observable<List<Place>> call(List<Place> places) {
                        return Observable.from(places)
                                .doOnNext(new Action1<Place>() {
                                    @Override
                                    public void call(Place place) {
                                        cachedPlaces.put(place._id(), place);
                                    }
                                })
                                .toList();
                    }
                });
    }
}
