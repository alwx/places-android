package me.alwx.places.ui.places;

import dagger.Subcomponent;
import me.alwx.places.ui.ActivityScope;
import me.alwx.places.ui.places.PlacesActivity;
import me.alwx.places.ui.places.PlacesActivityModule;

@ActivityScope
@Subcomponent(modules={ PlacesActivityModule.class })
public interface PlacesActivityComponent {
    void inject(PlacesActivity activity);
}