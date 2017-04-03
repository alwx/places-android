package me.alwx.places.ui.fragments.components;

import dagger.Subcomponent;
import me.alwx.places.ui.fragments.modules.PlacesListFragmentModule;
import me.alwx.places.ui.fragments.FragmentScope;
import me.alwx.places.ui.fragments.PlacesListFragment;

@FragmentScope
@Subcomponent(modules={ PlacesListFragmentModule.class })
public interface PlacesListFragmentComponent {
    void inject(PlacesListFragment activity);
}