package me.alwx.places.ui.places;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.ui.ActivityScope;

@Module
public class PlacesActivityModule {
    private PlacesActivity activity;

    public PlacesActivityModule(PlacesActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    PlacesActivityPresenter providePresenter(PlacesRepository repository) {
        return new PlacesActivityPresenter(activity, repository);
    }
}