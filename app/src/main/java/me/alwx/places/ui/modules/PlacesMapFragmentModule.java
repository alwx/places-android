package me.alwx.places.ui.modules;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.adapters.PlacesMapAdapter;
import me.alwx.places.ui.fragments.PlacesMapFragment;
import me.alwx.places.ui.presenters.PlacesMapFragmentPresenter;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PageInteractor;
import me.alwx.places.utils.PermissionsUtils;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@Module
public class PlacesMapFragmentModule {
    private PlacesMapFragment fragment;

    public PlacesMapFragmentModule(PlacesMapFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    PlacesMapFragmentPresenter providePresenter(PlacesRepository placesRepository,
                                                PermissionsUtils permissionsUtils,
                                                LocationUtils locationUtils,
                                                PageInteractor pageInteractor) {
        return new PlacesMapFragmentPresenter.Builder()
                .setFragment(fragment)
                .setPlacesRepository(placesRepository)
                .setPermissionsUtils(permissionsUtils)
                .setLocationUtils(locationUtils)
                .setPageInteractor(pageInteractor)
                .build();
    }

    @Provides
    @FragmentScope
    PlacesMapAdapter providePlacesMapAdapter() {
        return new PlacesMapAdapter(fragment);
    }

}