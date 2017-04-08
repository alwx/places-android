package me.alwx.places.ui.places;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;

import me.alwx.places.R;
import me.alwx.places.utils.PermissionsUtils;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesActivityPresenter {

    private PlacesActivity activity;
    private GoogleApiClient apiClient;
    private PermissionsUtils permissionsUtils;

    PlacesActivityPresenter(PlacesActivity activity,
                            GoogleApiClient apiClient,
                            PermissionsUtils permissionsUtils) {
        this.activity = activity;
        this.apiClient = apiClient;
        this.permissionsUtils = permissionsUtils;
    }

    void onRequestPermissionsResult(int requestCode,
                                    String[] permissions,
                                    int[] grantResults) {
        permissionsUtils.onRequestResult(requestCode, permissions, grantResults);
    }

    void onCreate() {
        initBottomBar();
        permissionsUtils.requestPermissions(
                activity,
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
}
