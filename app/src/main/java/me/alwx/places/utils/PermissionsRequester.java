package me.alwx.places.utils;

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

public class PermissionsRequester {
    private static final int PERMISSIONS_REQUEST = 1;

    private Context context;
    private EventBus eventBus;

    private Bundle state;

    public PermissionsRequester(Context context,
                                EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
    }

    public void requestPermissions(@NonNull final Activity activity,
                                   Bundle savedInstanceState,
                                   @NonNull final String[] permissions) {
        state = savedInstanceState;

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

    public Bundle getState() {
        return state;
    }

    private boolean isPermissionGranted(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    public class PermissionsGrantedEvent {

    }
}
