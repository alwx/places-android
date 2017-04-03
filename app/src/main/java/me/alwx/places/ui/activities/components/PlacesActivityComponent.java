package me.alwx.places.ui.activities.components;

import dagger.Subcomponent;
import me.alwx.places.ui.activities.ActivityScope;
import me.alwx.places.ui.activities.PlacesActivity;
import me.alwx.places.ui.activities.modules.PlacesActivityModule;

@ActivityScope
@Subcomponent(modules={ PlacesActivityModule.class })
public interface PlacesActivityComponent {
    void inject(PlacesActivity activity);
}