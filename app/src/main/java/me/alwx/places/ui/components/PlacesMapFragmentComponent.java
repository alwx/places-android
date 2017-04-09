package me.alwx.places.ui.components;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.modules.PlacesMapFragmentModule;
import me.alwx.places.ui.fragments.PlacesMapFragment;

@FragmentScope
@Subcomponent(modules={ PlacesMapFragmentModule.class })
public interface PlacesMapFragmentComponent {
    void inject(PlacesMapFragment activity);
}