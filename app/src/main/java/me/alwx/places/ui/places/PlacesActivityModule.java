package me.alwx.places.ui.places;

import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.di.scopes.ActivityScope;
import me.alwx.places.utils.PermissionsUtils;

@Module
public class PlacesActivityModule {
    private PlacesActivity activity;

    PlacesActivityModule(PlacesActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    PagerAdapter providePagerAdapter() {
        return new PagerAdapter(activity.getSupportFragmentManager());
    }

    @Provides
    @ActivityScope
    PlacesActivityPresenter providePresenter(GoogleApiClient apiClient,
                                             PermissionsUtils permissionsUtils) {
        return new PlacesActivityPresenter(activity, apiClient, permissionsUtils);
    }
}