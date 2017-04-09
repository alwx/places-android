package me.alwx.places.ui.presenters;


import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;

import me.alwx.places.R;
import me.alwx.places.ui.activities.PlacesActivity;
import me.alwx.places.ui.adapters.Page;
import me.alwx.places.ui.adapters.PlacesPagerAdapter;
import me.alwx.places.utils.PermissionsUtils;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesActivityPresenter {

    private PlacesActivity activity;
    private GoogleApiClient apiClient;
    private PermissionsUtils permissionsUtils;
    private PlacesPagerAdapter pagerAdapter;

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Page page = pagerAdapter.getPage(position);
            activity.setTitle(page.title());
            activity.setBottomNavItem(page.id());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public PlacesActivityPresenter(PlacesActivity activity,
                                   GoogleApiClient apiClient,
                                   PermissionsUtils permissionsUtils,
                                   PlacesPagerAdapter pagerAdapter) {
        this.activity = activity;
        this.apiClient = apiClient;
        this.permissionsUtils = permissionsUtils;
        this.pagerAdapter = pagerAdapter;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        permissionsUtils.onRequestResult(requestCode, permissions, grantResults);
    }

    public void onCreate() {
        activity.initPager(pagerAdapter);
        activity.setTitle(pagerAdapter.getPage(0).title());
        initBottomBar();

        permissionsUtils.requestPermissions(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );
    }

    public void onStart() {
        apiClient.connect();
        activity.setPagerCallbacks(onPageChangeListener);
    }

    public void onStop() {
        apiClient.disconnect();
        activity.clearPagerCallbacks(onPageChangeListener);
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
