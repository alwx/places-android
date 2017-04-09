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
 * @author alwx
 * @version 1.0
 */
public class PlacesActivity extends BaseActivity {
    @Inject
    PlacesActivityPresenter presenter;

    private ActivityMainBinding binding;

    @Override
    protected void initializeDependencyInjector() {
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initBottomBar(BottomNavigationView.OnNavigationItemSelectedListener listener) {
        binding.bottomNav.setOnNavigationItemSelectedListener(listener);
    }

    public void setBottomNavItem(@IdRes int itemId) {
        binding.bottomNav.setSelectedItemId(itemId);
    }

    public void initPager(PagerAdapter adapter) {
        binding.pager.setAdapter(adapter);
    }

    public void setPagerCallbacks(ViewPager.OnPageChangeListener listener) {
        binding.pager.addOnPageChangeListener(listener);
    }

    public void clearPagerCallbacks(ViewPager.OnPageChangeListener listener) {
        binding.pager.removeOnPageChangeListener(listener);
    }

    public void setPagerItem(int n) {
        binding.pager.setCurrentItem(n);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        binding.toolbar.setTitle(titleId);
    }
}
