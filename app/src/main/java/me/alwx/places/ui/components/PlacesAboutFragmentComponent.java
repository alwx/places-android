package me.alwx.places.ui.components;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.fragments.PlacesAboutFragment;
import me.alwx.places.ui.modules.PlacesAboutFragmentModule;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@FragmentScope
@Subcomponent(modules={PlacesAboutFragmentModule.class })
public interface PlacesAboutFragmentComponent {
    void inject(PlacesAboutFragment activity);
}