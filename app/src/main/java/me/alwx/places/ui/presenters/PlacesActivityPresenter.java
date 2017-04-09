package me.alwx.places.ui.presenters;


import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;

import me.alwx.places.R;
import me.alwx.places.ui.activities.PlacesActivity;
import me.alwx.places.data.models.inner.Page;
import me.alwx.places.ui.adapters.PlacesPagerAdapter;
import me.alwx.places.utils.PageNavigator;
import me.alwx.places.utils.PermissionsUtils;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesActivityPresenter {

    private PlacesActivity activity;
    private GoogleApiClient googleApiClient;
    private PermissionsUtils permissionsUtils;
    private PlacesPagerAdapter pagerAdapter;
    private PageNavigator pageNavigator;

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Page page = pagerAdapter.getPage(position);
            activity.setTitle(page.title());
            activity.setBottomNavItem(page.id());

            pageNavigator.navigatedTo(page);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private PlacesActivityPresenter(Builder builder) {
        this.activity = builder.activity;
        this.googleApiClient = builder.googleApiClient;
        this.permissionsUtils = builder.permissionsUtils;
        this.pagerAdapter = builder.pagerAdapter;
        this.pageNavigator = builder.pageNavigator;
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
        googleApiClient.connect();
        activity.setPagerCallbacks(onPageChangeListener);
    }

    public void onStop() {
        googleApiClient.disconnect();
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

    public static class Builder {
        private PlacesActivity activity;
        private GoogleApiClient googleApiClient;
        private PermissionsUtils permissionsUtils;
        private PlacesPagerAdapter pagerAdapter;
        private PageNavigator pageNavigator;

        public Builder setActivity(PlacesActivity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setGoogleApiClient(GoogleApiClient googleApiClient) {
            this.googleApiClient = googleApiClient;
            return this;
        }

        public Builder setPermissionsUtils(PermissionsUtils requester) {
            this.permissionsUtils = requester;
            return this;
        }

        public Builder setPagerAdapter(PlacesPagerAdapter pagerAdapter) {
            this.pagerAdapter = pagerAdapter;
            return this;
        }

        public Builder setPageNavigator(PageNavigator pageNavigator) {
            this.pageNavigator = pageNavigator;
            return this;
        }

        public PlacesActivityPresenter build() {
            return new PlacesActivityPresenter(this);
        }
    }
}
