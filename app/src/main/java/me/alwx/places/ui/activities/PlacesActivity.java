package me.alwx.places.ui.activities;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.data.repositories.PlaceRepository;
import me.alwx.places.databinding.ActivityMainBinding;
import me.alwx.places.data.network.DefaultApiInterface;
import me.alwx.places.data.network.GoogleApiInterface;
import me.alwx.places.ui.activities.modules.PlacesActivityModule;
import me.alwx.places.ui.presenters.PlacesActivityPresenter;
import me.alwx.places.ui.fragments.PlacesAboutFragment;
import me.alwx.places.ui.fragments.PlacesMapFragment;
import me.alwx.places.ui.fragments.PlacesListFragment;
import timber.log.Timber;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesActivity extends BaseActivity {
    @Inject
    DefaultApiInterface apiInterface;

    @Inject
    GoogleApiInterface googleGoogleApiInterface;

    @Inject
    GoogleApiClient googleApiClient;

    @Inject
    PlaceRepository placeRepository;

    @Inject
    PlacesActivityPresenter presenter;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initBottomBar();
        attachSectionFragment(Section.PLACES);
    }

    @Override
    protected void initializeDependencyInjector() {
        App.get(this).getPlacesComponent()
                .plus(new PlacesActivityModule(this))
                .inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void initBottomBar() {
        binding.bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_places:
                                attachSectionFragment(Section.PLACES);
                                break;
                            case R.id.action_map:
                                attachSectionFragment(Section.MAP);
                                break;
                            case R.id.action_about:
                                attachSectionFragment(Section.ABOUT);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void attachSectionFragment(@NonNull Section section) {
        Class<? extends Fragment> cls = section.getFragmentClass();
        Fragment fragment;
        try {
            fragment = cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Timber.e("Failed creating fragment instance: %s", e);
            return;
        }

        attachFragment(fragment);
        binding.toolbar.setTitle(section.getTitle());
    }

    private void attachFragment(@NonNull Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private enum Section {
        PLACES(PlacesListFragment.class, R.string.main_navigation_places),
        MAP(PlacesMapFragment.class, R.string.main_navigation_map),
        ABOUT(PlacesAboutFragment.class, R.string.main_navigation_about);

        private Class<? extends Fragment> fragmentClass;
        private int title;

        Section(Class<? extends Fragment> fragmentClass, @StringRes int title) {
            this.fragmentClass = fragmentClass;
            this.title = title;
        }

        public Class<? extends Fragment> getFragmentClass() {
            return fragmentClass;
        }

        public int getTitle() {
            return title;
        }
    }
}
