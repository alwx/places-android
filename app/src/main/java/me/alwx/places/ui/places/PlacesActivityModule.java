package me.alwx.places.ui.places;

import com.google.android.gms.common.api.GoogleApiClient;

import java.security.Permission;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.di.scopes.ActivityScope;
import me.alwx.places.utils.EventBus;
import me.alwx.places.utils.PermissionsRequester;

@Module
public class PlacesActivityModule {
    private PlacesActivity activity;

    public PlacesActivityModule(PlacesActivity activity) {
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
                                             PermissionsRequester permissionsRequester) {
        return new PlacesActivityPresenter(activity, apiClient, permissionsRequester);
    }
}