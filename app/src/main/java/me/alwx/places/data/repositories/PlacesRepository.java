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
 * This class contains methods that provide an access to {@link Place}
 * and related {@link Geodata} objects from local and remote data repositories.
 *
 * @author alwx
 * @version 1.0
 */
public class PlacesRepository {
    private PlacesLocalDataSource localDataSource;
    private PlacesRemoteDataSource remoteDataSource;

    private Map<Long, Place> cachedPlaces;
    private Map<String, Geodata> cachedGeodata;
    private boolean cacheIsDirty = false;

    private CompositeSubscription geoParamsSubscription;

    public PlacesRepository(@NonNull PlacesLocalDataSource localDataSource,
                            @NonNull PlacesRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

        geoParamsSubscription = new CompositeSubscription();
    }

    /**
     * Creates an Observable that uses cachedPlaces, remote and local repositores
     * to get Places.
     *
     * @return the new {@link Observable} instance
     */
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

    /**
     * Marks cache as dirty, and by doing this initiates the refreshing process.
     */
    public void refreshPlaces() {
        cacheIsDirty = true;
    }

    /**
     * Sends a request to remote repository and saves data to local repository
     * and to cachedPlaces map.
     *
     * @return the new {@link Observable} instance
     */
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

    /**
     * Creates an Observable which returns {@link Place} objects from local
     * repository when it is subscribed to.
     *
     * @return the new {@link Observable} instance
     */
    public Observable<List<Place>> getAndCacheLocalPlaces() {
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

    /**
     * Returns a {@link Subscription} to Geodata about a {@link Place}.
     * This methods requests Geodata from Google API and saves it to both local repository
     * and cachedGeodata map.
     *
     * @param place Place of interest
     *
     * @return the new {@link Subscription} instance
     */
    private Subscription getAndSaveRemoteGeodata(@NonNull final Place place) {
        final String addressStr = place.address().asString();
        if (cachedGeodata != null && cachedGeodata.containsKey(addressStr)) {
            return Observable.just(cachedGeodata.get(addressStr)).subscribe();
        } else if (cachedGeodata == null) {
            cachedGeodata = new LinkedHashMap<>();
        }

        return remoteDataSource
                .getGeoParams(addressStr)
                .flatMap(new Func1<GeocodeResponse, Observable<Geodata>>() {
                    @Override
                    public Observable<Geodata> call(GeocodeResponse resp) {
                        Geodata geodata = Geodata.builder()
                                .setId(place.id())
                                .setLatitude(resp.getLat())
                                .setLongitude(resp.getLng())
                                .setAddress(addressStr)
                                .build();

                        localDataSource.saveGeodata(geodata);
                        cachedGeodata.put(place.address().asString(), geodata);

                        return Observable.just(geodata);
                    }
                })
                .subscribe();
    }

    /**
     * Returns the Observable which observes for Geodata changes.
     * This method returns data from local repository.
     *
     * @return the new {@link Observable} instance
     */
    public Observable<List<Geodata>> getLocalGeodata() {
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
                                        cachedGeodata.put(geodata.address(), geodata);
                                    }
                                })
                                .toList();
                    }
                });
    }
}
