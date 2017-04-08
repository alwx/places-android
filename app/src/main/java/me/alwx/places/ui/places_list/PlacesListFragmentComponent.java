package me.alwx.places.ui.places_list;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.FragmentScope;

@FragmentScope
@Subcomponent(modules={ PlacesListFragmentModule.class })
public interface PlacesListFragmentComponent {
    void inject(PlacesListFragment activity);
}