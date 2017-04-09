package me.alwx.places.ui.presenters;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import me.alwx.places.R;
import me.alwx.places.ui.fragments.PlacesAboutFragment;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesAboutFragmentPresenter {
    private PlacesAboutFragment fragment;
    private Context appContext;

    public PlacesAboutFragmentPresenter(PlacesAboutFragment fragment,
                                        Context appContext) {
        this.fragment = fragment;
        this.appContext = appContext;
    }

    public void initializeViews() {
        fragment.setVersionText(fragment.getString(R.string.about_version, getVersionInfo()));
    }

    @Nullable
    private String getVersionInfo() {
        try {
            PackageInfo info = appContext.getPackageManager().getPackageInfo(
                    appContext.getPackageName(), 0
            );
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
