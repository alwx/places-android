package me.alwx.places.ui.components;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.modules.PlacesMapFragmentModule;
import me.alwx.places.ui.fragments.PlacesMapFragment;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@FragmentScope
@Subcomponent(modules={ PlacesMapFragmentModule.class })
public interface PlacesMapFragmentComponent {
    void inject(PlacesMapFragment activity);
}