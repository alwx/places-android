package me.alwx.places.ui.components;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.fragments.PlacesListFragment;
import me.alwx.places.ui.modules.PlacesListFragmentModule;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@FragmentScope
@Subcomponent(modules={ PlacesListFragmentModule.class })
public interface PlacesListFragmentComponent {
    void inject(PlacesListFragment activity);
}