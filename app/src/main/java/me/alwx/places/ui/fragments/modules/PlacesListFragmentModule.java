package me.alwx.places.ui.fragments.modules;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.api.PlacesManager;
import me.alwx.places.ui.fragments.FragmentScope;
import me.alwx.places.ui.adapters.PlacesAdapter;
import me.alwx.places.ui.fragments.PlacesListFragment;
import me.alwx.places.ui.fragments.presenters.PlacesListFragmentPresenter;

@Module
public class PlacesListFragmentModule {
    private PlacesListFragment fragment;

    public PlacesListFragmentModule(PlacesListFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    PlacesAdapter providePlacesAdapter() {
        return new PlacesAdapter(fragment);
    }

    @Provides
    @FragmentScope
    PlacesListFragmentPresenter providePresenter(PlacesManager placesManager) {
        return new PlacesListFragmentPresenter(fragment, placesManager);
    }
}