package me.alwx.places.ui.places_map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.utils.EventBus;
import me.alwx.places.utils.PermissionsRequester;
import me.alwx.places.utils.PermissionsRequester.PermissionsGrantedEvent;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesMapFragmentPresenter {
    private PlacesMapFragment fragment;
    private PlacesRepository placesRepository;
    private GoogleApiClient apiClient;
    private EventBus eventBus;
    private PermissionsRequester permissionsRequester;

    private Bundle state;
    private Subscription eventBusSubscription;

    private GoogleMap googleMap;
    private Location location;

    private PlacesMapFragmentPresenter(Builder builder) {
        this.fragment = builder.fragment;
        this.placesRepository = builder.placesRepository;
        this.apiClient = builder.apiClient;
        this.eventBus = builder.eventBus;
        this.permissionsRequester = builder.permissionsRequester;
    }

    void onCreate(Bundle state) {
        this.state = state;
        subscribeToEvents();
        initMap();
    }

    void onDestroy() {
        if (eventBusSubscription != null) {
            eventBusSubscription.unsubscribe();
            eventBusSubscription = null;
        }
    }

    private void subscribeToEvents() {
        eventBus.getEvents().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof PermissionsGrantedEvent) {
                    initMap();
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
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                permissionsRequester
                        .checkPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        })
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (granted) {
                                    // noinspection MissingPermission
                                    googleMap.setMyLocationEnabled(true);
                                    requestLocation();
                                }
                            }
                        });

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (location != null) {
                            animateTo(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                        //showPlacesFromDatabase();
                    }
                });
            }
        });
    }

    private void requestLocation() {
        // noinspection MissingPermission
        location = LocationServices.FusedLocationApi.getLastLocation(apiClient);

        if (location == null) {
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            request.setNumUpdates(1);

            // noinspection MissingPermission
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient,
                    request,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            PlacesMapFragmentPresenter.this.location = location;
                        }
                    }
            );
        }
    }

    private void animateTo(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        googleMap.animateCamera(cameraUpdate);
    }

    static class Builder {
        private PlacesMapFragment fragment;
        private PlacesRepository placesRepository;
        private GoogleApiClient apiClient;
        private EventBus eventBus;
        private PermissionsRequester permissionsRequester;

        Builder setFragment(PlacesMapFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        Builder setPlacesRepository(PlacesRepository repository) {
            this.placesRepository = repository;
            return this;
        }

        Builder setApiClient(GoogleApiClient apiClient) {
            this.apiClient = apiClient;
            return this;
        }

        Builder setEventBus(EventBus eventBus) {
            this.eventBus = eventBus;
            return this;
        }

        Builder setPermissionsRequester(PermissionsRequester requester) {
            this.permissionsRequester = requester;
            return this;
        }

        PlacesMapFragmentPresenter build() {
            return new PlacesMapFragmentPresenter(this);
        }
    }
}
