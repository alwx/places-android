package me.alwx.places.ui.places;


import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import me.alwx.places.R;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.ui.Presenter;
import me.alwx.places.ui.places_map.PlacesMapFragmentPresenter;
import me.alwx.places.utils.EventBus;
import me.alwx.places.utils.PermissionsRequester;
import rx.functions.Func0;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesActivityPresenter {

    private PlacesActivity activity;
    private GoogleApiClient apiClient;
    private PermissionsRequester permissionsRequester;

    PlacesActivityPresenter(PlacesActivity activity,
                            GoogleApiClient apiClient,
                            PermissionsRequester permissionsRequester) {
        this.activity = activity;
        this.apiClient = apiClient;
        this.permissionsRequester = permissionsRequester;
    }

    void onRequestPermissionsResult(int requestCode,
                                    String[] permissions,
                                    int[] grantResults) {
        permissionsRequester.onRequestResult(requestCode, permissions, grantResults);
    }

    void onCreate(Bundle savedInstanceState) {
        initBottomBar();
        permissionsRequester.requestPermissions(
                activity,
                savedInstanceState,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }
        );
    }

    void onStart() {
        apiClient.connect();
    }

    void onStop() {
        apiClient.disconnect();
    }

    private void initBottomBar() {
        activity.initBottomBar(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_places:
                                activity.setPagerItem(0);
                                break;
                            case R.id.action_map:
                                activity.setPagerItem(1);
                                break;
                            case R.id.action_about:
                                activity.setPagerItem(2);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void initMap() {
        Handler initMapHandler = new Handler();
        Runnable initMapRunnable = new Runnable() {
            @Override
            public void run() {
                initMapAsync(permissionsRequester.getState());
            }
        };
        initMapHandler.post(initMapRunnable);
    }

    private void initMapAsync(final Bundle state) {
        /*fragment.initMap(state, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                PlacesMapFragmentPresenter.this.googleMap = googleMap;
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // noinspection MissingPermission
                    googleMap.setMyLocationEnabled(true);

                    requestLocation();
                }

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (location != null) {
                            animateTo(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                });
            }
        });

        MapsInitializer.initialize(fragment.getActivity());*/
    }
}
