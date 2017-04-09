package me.alwx.places.ui.modules;

import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.R;
import me.alwx.places.di.scopes.ActivityScope;
import me.alwx.places.data.models.inner.Page;
import me.alwx.places.ui.adapters.PlacesPagerAdapter;
import me.alwx.places.ui.activities.PlacesActivity;
import me.alwx.places.ui.fragments.PlacesAboutFragment;
import me.alwx.places.ui.fragments.PlacesListFragment;
import me.alwx.places.ui.fragments.PlacesMapFragment;
import me.alwx.places.ui.presenters.PlacesActivityPresenter;
import me.alwx.places.utils.PermissionsUtils;

@Module
public class PlacesActivityModule {
    private PlacesActivity activity;

    public PlacesActivityModule(PlacesActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    PlacesPagerAdapter providePagerAdapter() {
        return new PlacesPagerAdapter(
                activity.getSupportFragmentManager(),
                Page.create(
                        PlacesListFragment.newInstance(),
                        R.id.action_places,
                        R.string.main_navigation_places
                ),
                Page.create(
                        PlacesMapFragment.newInstance(),
                        R.id.action_map,
                        R.string.main_navigation_map
                ),
                Page.create(
                        PlacesAboutFragment.newInstance(),
                        R.id.action_about,
                        R.string.main_navigation_about
                )
        );
    }

    @Provides
    @ActivityScope
    PlacesActivityPresenter providePresenter(GoogleApiClient apiClient,
                                             PermissionsUtils permissionsUtils,
                                             PlacesPagerAdapter pagerAdapter) {
        return new PlacesActivityPresenter(activity, apiClient, permissionsUtils, pagerAdapter);
    }
}