package me.alwx.places.di;

import dagger.Subcomponent;
import me.alwx.places.ui.places.PlacesActivityComponent;
import me.alwx.places.ui.places.PlacesActivityModule;
import me.alwx.places.ui.places_list.PlacesListFragmentComponent;
import me.alwx.places.ui.places_list.PlacesListFragmentModule;

@DataScope
@Subcomponent(modules={ PlacesModule.class })
public interface PlacesComponent {
    PlacesActivityComponent plus(PlacesActivityModule module);

    PlacesListFragmentComponent plus(PlacesListFragmentModule module);
}