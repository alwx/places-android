package me.alwx.places.ui.places_map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import me.alwx.places.data.models.Place;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PermissionsUtils;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesMapFragmentPresenter {
    private PlacesMapFragment fragment;
    private PlacesRepository placesRepository;
    private PermissionsUtils permissionsUtils;
    private LocationUtils locationUtils;

    private Bundle state;

    private GoogleMap googleMap;

    private Subscription permissionSubscription;
    private Subscription locationSubscription;

    private PlacesMapFragmentPresenter(Builder builder) {
        this.fragment = builder.fragment;
        this.placesRepository = builder.placesRepository;
        this.permissionsUtils = builder.permissionsUtils;
        this.locationUtils = builder.locationUtils;
    }

    void onCreate(Bundle state) {
        this.state = state;
        subscribeToEvents();
    }

    void onResume() {
        locationUtils.startReceivingUpdates();
        initMap();
    }

    void onPause() {
        locationUtils.stopReceivingUpdates();
    }

    void onDestroy() {
        if (permissionSubscription != null) {
            permissionSubscription.unsubscribe();
            permissionSubscription = null;
        }
        if (locationSubscription != null) {
            locationSubscription.unsubscribe();
            locationSubscription = null;
        }
    }

    private void subscribeToEvents() {
        permissionSubscription = permissionsUtils.getRequestResults().subscribe(
                new Action1<String[]>() {
                    @Override
                    public void call(String[] strings) {
                        initMap();
                        locationUtils.startReceivingUpdates();
                    }
                }
        );
        locationSubscription = locationUtils.getLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                if (location != null) {
                    animateTo(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    private void initMap() {
        Handler initMapHandler = new Handler();
        Runnable initMapRunnable = new Runnable() {
            @Override
            public void run() {
                initMapAsync();
            }
        };
        initMapHandler.post(initMapRunnable);
    }

    private void initMapAsync() {
        fragment.initMap(state, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                permissionsUtils
                        .checkPermissions(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (granted) {
                                    // noinspection MissingPermission
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                                }
                            }
                        });

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        loadPlaces();
                    }
                });
            }
        });
    }

    private void animateTo(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        googleMap.animateCamera(cameraUpdate);
    }

    private void loadPlaces() {
        Subscription subscription = placesRepository
                .getPlaces()
                .flatMap(new Func1<List<Place>, Observable<Place>>() {
                    @Override
                    public Observable<Place> call(List<Place> places) {
                        return Observable.from(places);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Place>>() {
                    @Override
                    public void call(List<Place> places) {
                        if (googleMap == null) {
                            return;
                        }
                        googleMap.clear();

                        List<Marker> markerList = new ArrayList<>();
                        for (int i = 0; i < places.size(); i++) {
                            Place p = places.get(i);
                            /*markerList.add(
                                    googleMap.addMarker(
                                            new MarkerOptions()
                                                    .position(new LatLng(p.getLat(), p.getLng()))
                                                    .title(String.valueOf(i))
                                    )
                            );*/
                        }

                        if (markerList.size() > 0) {
                            animateTo(markerList.get(0).getPosition());
                        }
                    }
                });
    }

    static class Builder {
        private PlacesMapFragment fragment;
        private PlacesRepository placesRepository;
        private PermissionsUtils permissionsUtils;
        private LocationUtils locationUtils;

        Builder setFragment(PlacesMapFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        Builder setPlacesRepository(PlacesRepository repository) {
            this.placesRepository = repository;
            return this;
        }

        Builder setPermissionsUtils(PermissionsUtils requester) {
            this.permissionsUtils = requester;
            return this;
        }

        Builder setLocationUtils(LocationUtils locationUtils) {
            this.locationUtils = locationUtils;
            return this;
        }

        PlacesMapFragmentPresenter build() {
            return new PlacesMapFragmentPresenter(this);
        }
    }
}
