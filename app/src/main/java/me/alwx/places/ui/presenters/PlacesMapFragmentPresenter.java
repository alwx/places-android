package me.alwx.places.ui.presenters;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;

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
    private Location location;
    private List<Marker> markerList = new ArrayList<>();

    private CompositeSubscription moduleSubscriptions = new CompositeSubscription();
    private CompositeSubscription dataSubscriptions = new CompositeSubscription();

    /**
     * This listener listens for selected Place changes.
     * TODO: inject it with Dagger
     */
    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position < markerList.size()) {
                Marker marker = markerList.get(position);
                animateTo(marker.getPosition());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private PlacesMapFragmentPresenter(Builder builder) {
        this.fragment = builder.fragment;
        this.placesRepository = builder.placesRepository;
        this.permissionsUtils = builder.permissionsUtils;
        this.locationUtils = builder.locationUtils;
        this.pageInteractor = builder.pageInteractor;
    }

    /**
     * Delegate method for the standard Android lifecycle onCreate() call.
     */
    public void onCreate(Bundle state) {
        this.state = state;
        subscribeToEvents();
    }

    /**
     * Delegate method for the standard Android lifecycle onResume() call.
     */
    public void onResume() {
        locationUtils.startReceivingUpdates();
        initMap();
        fragment.setPagerCallbacks(onPageChangeListener);
    }

    /**
     * Delegate method for the standard Android lifecycle onPause() call.
     */
    public void onPause() {
        locationUtils.stopReceivingUpdates();
        fragment.clearPagerCallbacks(onPageChangeListener);
    }

    /**
     * Delegate method for the standard Android lifecycle onDestroy() call.
     */
    public void onDestroy() {
        moduleSubscriptions.clear();
        dataSubscriptions.clear();
    }

    /**
     * Subscribes to several important events like permission request results, location
     * request results and page interactor events.
     *
     * @see PageInteractor
     * @see LocationUtils
     * @see PermissionsUtils
     */
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
                        PlacesMapFragmentPresenter.this.location = location;
                        fragment.setAdapterLocation(location);
                    }
                });
        Subscription pageNavigatorSubscription = pageInteractor
                .getPlacesLoadedEvents()
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void _void) {
                        addMapPlaces();
                    }
                });
        Subscription pageMapSubscription = pageInteractor
                .getGoToMapPlaceEvents()
                .subscribe(new Action1<Place>() {
                    @Override
                    public void call(Place place) {
                        int pos = fragment.getAdapterPosition(place.id());
                        if (pos != -1) {
                            Marker marker = markerList.get(pos);
                            animateTo(marker.getPosition());

                            fragment.pagerNavigateTo(pos);
                        }
                    }
                });

        moduleSubscriptions.add(permissionSubscription);
        moduleSubscriptions.add(locationSubscription);
        moduleSubscriptions.add(pageNavigatorSubscription);
        moduleSubscriptions.add(pageMapSubscription);
    }

    /**
     * Initializes Google map.
     */
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

    /**
     * Continues intitializing map. Should be called only from initMap()
     */
    private void initMapAsync() {
        fragment.initMap(state, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                googleMap.getUiSettings().setZoomControlsEnabled(true);

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

    /**
     * Adds places to the map. Map should be initialized.
     */
    private void addMapPlaces() {
        googleMap.clear();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                long id = Integer.valueOf(marker.getTitle());
                int pos = fragment.getAdapterPosition(id);
                if (pos != -1) {
                    fragment.pagerNavigateTo(pos);
                }

                return true;
            }
        });
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                loadPlaces();

                if (location != null) {
                    animateTo(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    /**
     * Animates camera to a given position.
     *
     * @param latLng position
     */
    private void animateTo(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        googleMap.animateCamera(cameraUpdate);
    }

    /**
     * Subscribes to places and geodata changes in the database and updates the UI if needed.
     */
    @SuppressWarnings("ConstantConditions")
    private void loadPlaces() {
        dataSubscriptions.clear();

        Subscription geodataSubscription = placesRepository.getLocalGeodata()
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

    /**
     * Because telescoping constructor is a bad pattern, we use this Builder
     * to construct a presenter.
     */
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
