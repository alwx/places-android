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
 * @author alwx
 * @version 1.0
 */

public class PermissionsUtils {
    private static final int PERMISSIONS_REQUEST = 1;

    private Context context;
    private PublishSubject<String[]> subject = PublishSubject.create();

    public PermissionsUtils(Context context) {
        this.context = context;
    }

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

    public Observable<Boolean> checkPermissions(@NonNull final String... permissions) {
        return Observable.from(permissions).all(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return isPermissionGranted(s);
            }
        });
    }

    public Observable<String[]> getRequestResults() {
        return subject;
    }

    public void onRequestResult(int requestCode,
                                String[] permissions,
                                int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                subject.onNext(permissions);
                break;
        }
    }

    private boolean isPermissionGranted(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }
}
