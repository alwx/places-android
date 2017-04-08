package me.alwx.places.ui.places_map;

import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.utils.EventBus;
import me.alwx.places.utils.PermissionsRequester;

@Module
public class PlacesMapFragmentModule {
    private PlacesMapFragment fragment;

    PlacesMapFragmentModule(PlacesMapFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    PlacesMapFragmentPresenter providePresenter(PlacesRepository placesRepository,
                                                GoogleApiClient apiClient,
                                                EventBus eventBus,
                                                PermissionsRequester requester) {
        return new PlacesMapFragmentPresenter.Builder()
                .setFragment(fragment)
                .setPlacesRepository(placesRepository)
                .setApiClient(apiClient)
                .setEventBus(eventBus)
                .setPermissionsRequester(requester)
                .build();
    }
}