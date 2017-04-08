package me.alwx.places.ui.places_map;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.FragmentScope;

@FragmentScope
@Subcomponent(modules={ PlacesMapFragmentModule.class })
public interface PlacesMapFragmentComponent {
    void inject(PlacesMapFragment activity);
}