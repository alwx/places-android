package me.alwx.places.ui.modules;

import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.fragments.PlacesMapFragment;
import me.alwx.places.ui.presenters.PlacesMapFragmentPresenter;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PermissionsUtils;

@Module
public class PlacesMapFragmentModule {
    private PlacesMapFragment fragment;

    public PlacesMapFragmentModule(PlacesMapFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    PlacesMapFragmentPresenter providePresenter(PlacesRepository placesRepository,
                                                GoogleApiClient apiClient,
                                                PermissionsUtils permissionsUtils,
                                                LocationUtils locationUtils) {
        return new PlacesMapFragmentPresenter.Builder()
                .setFragment(fragment)
                .setPlacesRepository(placesRepository)
                .setPermissionsUtils(permissionsUtils)
                .setLocationUtils(locationUtils)
                .build();
    }
}