package me.alwx.places.ui.components;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.ActivityScope;
import me.alwx.places.ui.modules.PlacesActivityModule;
import me.alwx.places.ui.activities.PlacesActivity;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@ActivityScope
@Subcomponent(modules={ PlacesActivityModule.class })
public interface PlacesActivityComponent {
    void inject(PlacesActivity activity);
}