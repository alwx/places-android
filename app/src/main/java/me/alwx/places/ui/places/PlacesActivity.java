package me.alwx.places.ui.places;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.databinding.ActivityMainBinding;
import me.alwx.places.ui.BaseActivity;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesActivity extends BaseActivity {
    @Inject
    PlacesActivityPresenter presenter;

    @Inject
    PagerAdapter pagerAdapter;

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
        binding.pager.setAdapter(pagerAdapter);
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

    void initBottomBar(BottomNavigationView.OnNavigationItemSelectedListener listener) {
        binding.bottomNav.setOnNavigationItemSelectedListener(listener);
    }

    void setPagerItem(int n) {
        binding.pager.setCurrentItem(n);
    }
}
