package me.alwx.places.ui.components;

import dagger.Subcomponent;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.fragments.PlacesAboutFragment;
import me.alwx.places.ui.modules.PlacesAboutFragmentModule;

@FragmentScope
@Subcomponent(modules={PlacesAboutFragmentModule.class })
public interface PlacesAboutFragmentComponent {
    void inject(PlacesAboutFragment activity);
}