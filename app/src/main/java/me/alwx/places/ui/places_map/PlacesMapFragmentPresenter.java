package me.alwx.places.ui.places_map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PermissionsUtils;
import rx.Subscription;
import rx.functions.Action1;

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
                        /*if (location != null) {
                            animateTo(new LatLng(location.getLatitude(), location.getLongitude()));
                        }*/
                        //showPlacesFromDatabase();
                    }
                });
            }
        });
    }

    private void animateTo(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        googleMap.animateCamera(cameraUpdate);
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
