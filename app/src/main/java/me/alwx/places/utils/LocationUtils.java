package me.alwx.places.utils;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import rx.functions.Action1;

/**
 * @author alwx
 * @version 1.0
 */

public class LocationUtils {
    private EventBus eventBus;
    private GoogleApiClient apiClient;
    private PermissionsUtils permissionsUtils;

    private ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            // noinspection MissingPermission
            Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            eventBus.addEvent(new LocationChangedEvent(location));

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
                            eventBus.addEvent(new LocationChangedEvent(location));
                        }
                    }
            );
        }

        @Override
        public void onConnectionSuspended(int i) {
            // do nothing
        }
    };

    public LocationUtils(EventBus eventBus,
                         GoogleApiClient apiClient,
                         PermissionsUtils permissionsUtils) {
        this.eventBus = eventBus;
        this.apiClient = apiClient;
        this.permissionsUtils = permissionsUtils;
    }

    public void requestLocationUpdates() {
        permissionsUtils
                .checkPermissions(PermissionsUtils.LOCATION_PERMISSIONS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isGranted) {
                        if (isGranted) {
                            apiClient.registerConnectionCallbacks(connectionCallbacks);
                        }
                    }
                });
    }

    public void stopLocationUpdates() {
        apiClient.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public class LocationChangedEvent {
        private Location location;

        LocationChangedEvent(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }
    }
}
