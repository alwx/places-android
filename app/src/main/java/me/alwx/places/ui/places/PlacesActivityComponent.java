package me.alwx.places.ui.places;

import dagger.Subcomponent;
import me.alwx.places.di.modules.LocationModule;
import me.alwx.places.di.scopes.ActivityScope;

@ActivityScope
@Subcomponent(modules={ PlacesActivityModule.class })
public interface PlacesActivityComponent {
    void inject(PlacesActivity activity);
}