package me.alwx.places.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @author alwx
 * @version 1.0
 */

public class PermissionsUtils {
    private static final int PERMISSIONS_REQUEST = 1;

    public static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Context context;
    private EventBus eventBus;

    public PermissionsUtils(Context context,
                            EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
    }

    public void requestPermissions(@NonNull final Activity activity,
                                   @NonNull final String[] permissions) {
        checkPermissions(permissions).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (!granted) {
                    ActivityCompat.requestPermissions(
                            activity,
                            permissions,
                            PERMISSIONS_REQUEST
                    );
                } else {
                    eventBus.addEvent(new PermissionsGrantedEvent());
                }
            }
        });
    }

    public Observable<Boolean> checkPermissions(@NonNull final String[] permissions) {
        return Observable.from(permissions).all(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return isPermissionGranted(s);
            }
        });
    }

    public void onRequestResult(int requestCode,
                                String[] permissions,
                                int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                eventBus.addEvent(new PermissionsGrantedEvent());
                break;
        }
    }

    private boolean isPermissionGranted(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    public class PermissionsGrantedEvent {

    }
}
