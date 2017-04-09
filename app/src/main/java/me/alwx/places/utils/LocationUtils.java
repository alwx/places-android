package me.alwx.places.utils;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * This class requests and updates location.
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class LocationUtils {
    private GoogleApiClient apiClient;
    private PermissionsUtils permissionsUtils;

    private ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            // noinspection MissingPermission
            Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            subject.onNext(location);

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
                            subject.onNext(location);
                        }
                    }
            );
        }

        @Override
        public void onConnectionSuspended(int i) {
            // do nothing
        }
    };
    private PublishSubject<Location> subject = PublishSubject.create();

    public LocationUtils(GoogleApiClient apiClient,
                         PermissionsUtils permissionsUtils) {
        this.apiClient = apiClient;
        this.permissionsUtils = permissionsUtils;
    }

    /**
     * Starts listening for location updates.
     */
    public void startReceivingUpdates() {
        permissionsUtils
                .checkPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isGranted) {
                        if (isGranted) {
                            apiClient.registerConnectionCallbacks(connectionCallbacks);
                        }
                    }
                });
    }

    /**
     * Stops listening for location updates.
     */
    public void stopReceivingUpdates() {
        apiClient.unregisterConnectionCallbacks(connectionCallbacks);
    }

    /**
     * Returns the Observable with the location.
     *
     * @return the new {@link Observable} instance
     */
    public Observable<Location> getLocation() {
        return subject;
    }
}
