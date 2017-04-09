package me.alwx.places.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.databinding.ActivityMainBinding;
import me.alwx.places.ui.adapters.PlacesPagerAdapter;
import me.alwx.places.ui.modules.PlacesActivityModule;
import me.alwx.places.ui.presenters.PlacesActivityPresenter;

/**
 * The main activity. Contains a ViewPager with several fragments.
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesActivity extends BaseActivity {
    @Inject
    PlacesActivityPresenter presenter;

    private ActivityMainBinding binding;

    @Override
    protected void initDependencyInjector() {
        App.get(this).getPlacesComponent()
                .plus(new PlacesActivityModule(this))
                .inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        presenter.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Sets listener for bottom navigation bar.
     * To be called by {@link PlacesActivityPresenter}
     *
     * @param listener on navigation item selected listener
     */
    public void initBottomBar(BottomNavigationView.OnNavigationItemSelectedListener listener) {
        binding.bottomNav.setOnNavigationItemSelectedListener(listener);
    }

    /**
     * Selects a navigation item.
     * To be called by {@link PlacesActivityPresenter}
     *
     * @param itemId Android ID of an item
     */
    public void setBottomNavItem(@IdRes int itemId) {
        binding.bottomNav.setSelectedItemId(itemId);
    }

    /**
     * Sets adapter for view pager and sets its offscreen page limit.
     * To be called by {@link PlacesActivityPresenter}
     *
     * @param adapter {@link PagerAdapter} instance
     */
    public void initPager(PagerAdapter adapter) {
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(2);
    }

    /**
     * Sets {@link ViewPager.OnPageChangeListener} listener for view pager.
     * To be called by {@link PlacesActivityPresenter}
     *
     * @param listener on page change listener
     */
    public void setPagerCallbacks(ViewPager.OnPageChangeListener listener) {
        binding.pager.addOnPageChangeListener(listener);
    }

    /**
     * Removes the callback.
     * To be called by {@link PlacesActivityPresenter}
     *
     * @param listener on page change listener
     */
    public void clearPagerCallbacks(ViewPager.OnPageChangeListener listener) {
        binding.pager.removeOnPageChangeListener(listener);
    }

    /**
     * Selects a pager item.
     * To be called by {@link PlacesActivityPresenter}
     *
     * @param n page index
     */
    public void setPagerItem(int n) {
        binding.pager.setCurrentItem(n);
    }

    /**
     * Sets the title of the activity.
     * To be called by {@link PlacesActivityPresenter}
     *
     * @param titleId title resource
     */
    @Override
    public void setTitle(@StringRes int titleId) {
        binding.toolbar.setTitle(titleId);
    }
}
