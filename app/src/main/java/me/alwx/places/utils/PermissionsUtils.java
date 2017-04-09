package me.alwx.places.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * This class controls runtime permissions.
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PermissionsUtils {
    private static final int PERMISSIONS_REQUEST = 1;

    private Context context;
    private PublishSubject<String[]> subject = PublishSubject.create();

    public PermissionsUtils(Context context) {
        this.context = context;
    }

    /**
     * Requests the required permissions.
     *
     * @param activity    current activity (the activity should forward onRequestResult method)
     * @param permissions required permissions
     */
    public void requestPermissions(@NonNull final Activity activity,
                                   @NonNull final String... permissions) {
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
                    subject.onNext(permissions);
                }
            }
        });
    }

    /**
     * Checks the permissions.
     *
     * @param permissions required permissions
     */
    public Observable<Boolean> checkPermissions(@NonNull final String... permissions) {
        return Observable.from(permissions).all(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return isPermissionGranted(s);
            }
        });
    }

    /**
     * Returns an Observable with request results.
     *
     * @return the new {@link Observable} instance
     */
    public Observable<String[]> getRequestResults() {
        return subject;
    }

    /**
     * Checks the request result.
     * This method should be called in activity's onRequestResult.
     *
     * @param requestCode  request code
     * @param permissions  required permissions
     * @param grantResults result
     */
    public void onRequestResult(int requestCode,
                                String[] permissions,
                                int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                subject.onNext(permissions);
                break;
        }
    }

    /**
     * Checks if permission is granted.
     *
     * @param permission required permission
     * @return true/false value
     */
    private boolean isPermissionGranted(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }
}
