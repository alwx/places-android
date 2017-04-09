package me.alwx.places.ui.presenters;

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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.data.models.inner.Page;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.ui.fragments.PlacesMapFragment;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PageInteractor;
import me.alwx.places.utils.PermissionsUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesMapFragmentPresenter {
    private PlacesMapFragment fragment;
    private PlacesRepository placesRepository;
    private PermissionsUtils permissionsUtils;
    private LocationUtils locationUtils;
    private PageInteractor pageInteractor;

    private Bundle state;
    private GoogleMap googleMap;
    private List<Marker> markerList = new ArrayList<>();
    private boolean firstTime = true;

    private CompositeSubscription moduleSubscriptions = new CompositeSubscription();
    private CompositeSubscription dataSubscriptions = new CompositeSubscription();

    private PlacesMapFragmentPresenter(Builder builder) {
        this.fragment = builder.fragment;
        this.placesRepository = builder.placesRepository;
        this.permissionsUtils = builder.permissionsUtils;
        this.locationUtils = builder.locationUtils;
        this.pageInteractor = builder.pageInteractor;
    }

    public void onCreate(Bundle state) {
        this.state = state;
        subscribeToEvents();
    }

    public void onResume() {
        locationUtils.startReceivingUpdates();
        initMap();
    }

    public void onPause() {
        locationUtils.stopReceivingUpdates();
        dataSubscriptions.clear();
    }

    public void onDestroy() {
        if (moduleSubscriptions != null) {
            moduleSubscriptions.unsubscribe();
            moduleSubscriptions = null;
        }
    }

    private void subscribeToEvents() {
        Subscription permissionSubscription = permissionsUtils
                .getRequestResults()
                .subscribe(
                        new Action1<String[]>() {
                            @Override
                            public void call(String[] strings) {
                                initMap();
                                locationUtils.startReceivingUpdates();
                            }
                        }
                );
        Subscription locationSubscription = locationUtils
                .getLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        fragment.setAdapterLocation(location);
                        if (location != null) {
                            animateTo(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                });

        Subscription pageNavigatorSubscription = pageInteractor
                .getPlacesLoadedEvents()
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void _void) {
                        initMapPlaces();
                    }
                });
        Subscription pageMapSubscription = pageInteractor
                .getGoToMapPlaceEvents()
                .subscribe(new Action1<Place>() {
                    @Override
                    public void call(Place place) {
                        fragment.pagerNavigateTo(place.id());
                    }
                });

        moduleSubscriptions.add(permissionSubscription);
        moduleSubscriptions.add(locationSubscription);
        moduleSubscriptions.add(pageNavigatorSubscription);
        moduleSubscriptions.add(pageMapSubscription);
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
            }
        });
    }

    private void initMapPlaces() {
        googleMap.clear();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                long id = Integer.valueOf(marker.getTitle());
                fragment.pagerNavigateTo(id);

                return true;
            }
        });
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                loadPlaces();
            }
        });
    }

    private void animateTo(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        googleMap.animateCamera(cameraUpdate);
    }

    @SuppressWarnings("ConstantConditions")
    private void loadPlaces() {
        dataSubscriptions.clear();

        Subscription geodataSubscription = placesRepository.getGeodata()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Geodata>>() {
                    @Override
                    public void call(List<Geodata> geodataList) {
                        fragment.setAdapterGeodataList(geodataList);
                        markerList = new ArrayList<>();
                        for (Geodata geodata : geodataList) {
                            LatLng latLng = new LatLng(geodata.latitude(), geodata.longitude());
                            markerList.add(
                                    googleMap.addMarker(
                                            new MarkerOptions()
                                                    .position(latLng)
                                                    .title(String.valueOf(geodata.id()))
                                    )
                            );
                        }
                    }
                });

        Subscription placesSubscription = placesRepository.getAndCacheLocalPlaces()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Place>>() {
                    @Override
                    public void call(List<Place> places) {
                        fragment.initPager(places);
                    }
                });

        dataSubscriptions.add(geodataSubscription);
        dataSubscriptions.add(placesSubscription);
    }

    public static class Builder {
        private PlacesMapFragment fragment;
        private PlacesRepository placesRepository;
        private PermissionsUtils permissionsUtils;
        private LocationUtils locationUtils;
        private PageInteractor pageInteractor;

        public Builder setFragment(PlacesMapFragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder setPlacesRepository(PlacesRepository repository) {
            this.placesRepository = repository;
            return this;
        }

        public Builder setPermissionsUtils(PermissionsUtils requester) {
            this.permissionsUtils = requester;
            return this;
        }

        public Builder setLocationUtils(LocationUtils locationUtils) {
            this.locationUtils = locationUtils;
            return this;
        }

        public Builder setPageInteractor(PageInteractor pageInteractor) {
            this.pageInteractor = pageInteractor;
            return this;
        }

        public PlacesMapFragmentPresenter build() {
            return new PlacesMapFragmentPresenter(this);
        }
    }
}
