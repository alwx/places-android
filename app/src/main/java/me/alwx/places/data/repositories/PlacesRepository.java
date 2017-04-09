package me.alwx.places.data.repositories;

import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.alwx.places.data.models.Address;
import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.data.models.gson.GeocodeResponse;
import me.alwx.places.data.repositories.local.PlacesLocalDataSource;
import me.alwx.places.data.repositories.remote.PlacesRemoteDataSource;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesRepository {
    private PlacesLocalDataSource localDataSource;
    private PlacesRemoteDataSource remoteDataSource;

    private Map<Long, Place> cachedPlaces;
    private Map<Long, Geodata> cachedGeodata;
    private boolean cacheIsDirty = false;

    private CompositeSubscription geoParamsSubscription;

    public PlacesRepository(PlacesLocalDataSource localDataSource,
                            PlacesRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

        geoParamsSubscription = new CompositeSubscription();
    }

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

    public void refreshPlaces() {
        cacheIsDirty = true;
    }

    private Observable<List<Place>> getAndSaveRemotePlaces() {
        return remoteDataSource
                .getPlaces()
                .flatMap(new Func1<List<Place>, Observable<List<Place>>>() {
                    @Override
                    public Observable<List<Place>> call(List<Place> places) {
                        localDataSource.removeAll();

                        return Observable.from(places).doOnNext(new Action1<Place>() {
                            @Override
                            public void call(Place place) {
                                geoParamsSubscription.add(getAndSaveRemoteGeodata(place));

                                localDataSource.savePlace(place);
                                cachedPlaces.put(place.id(), place);
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
                                        cachedPlaces.put(place.id(), place);
                                    }
                                })
                                .toList();
                    }
                });
    }

    private Subscription getAndSaveRemoteGeodata(@NonNull final Place place) {
        return remoteDataSource
                .getGeoParams(place.address().asString())
                .flatMap(new Func1<GeocodeResponse, Observable<Geodata>>() {
                    @Override
                    public Observable<Geodata> call(GeocodeResponse resp) {
                        Geodata geodata = Geodata.builder()
                                .setId(place.id())
                                .setLatitude(resp.getLat())
                                .setLongitude(resp.getLng())
                                .build();

                        localDataSource.saveGeodata(geodata);

                        if (cachedGeodata == null) {
                            cachedGeodata = new LinkedHashMap<>();
                        }
                        cachedGeodata.put(geodata.id(), geodata);

                        return Observable.just(geodata);
                    }
                })
                .subscribe();
    }

    public Observable<List<Geodata>> getGeodata() {
        return localDataSource
                .getGeodata()
                .flatMap(new Func1<List<Geodata>, Observable<List<Geodata>>>() {
                    @Override
                    public Observable<List<Geodata>> call(List<Geodata> geodata) {
                        return Observable.from(geodata)
                                .doOnNext(new Action1<Geodata>() {
                                    @Override
                                    public void call(Geodata geodata) {
                                        if (cachedGeodata == null) {
                                            cachedGeodata = new LinkedHashMap<>();
                                        }
                                        cachedGeodata.put(geodata.id(), geodata);
                                    }
                                })
                                .toList();
                    }
                });
    }
}
