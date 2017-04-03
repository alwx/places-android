package me.alwx.places.ui.activities.modules;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.ui.activities.ActivityScope;
import me.alwx.places.ui.activities.PlacesActivity;
import me.alwx.places.ui.activities.presenters.PlacesActivityPresenter;

@Module
public class PlacesActivityModule {
    private PlacesActivity activity;

    public PlacesActivityModule(PlacesActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    PlacesActivityPresenter providePresenter() {
        return new PlacesActivityPresenter(activity);
    }
}