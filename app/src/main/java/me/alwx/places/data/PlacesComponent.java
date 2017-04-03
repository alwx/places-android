package me.alwx.places.data;

import dagger.Subcomponent;
import me.alwx.places.ui.activities.components.PlacesActivityComponent;
import me.alwx.places.ui.activities.modules.PlacesActivityModule;
import me.alwx.places.ui.fragments.components.PlacesListFragmentComponent;
import me.alwx.places.ui.fragments.modules.PlacesListFragmentModule;

@DataScope
@Subcomponent(modules={ PlacesModule.class })
public interface PlacesComponent {
    PlacesActivityComponent plus(PlacesActivityModule module);

    PlacesListFragmentComponent plus(PlacesListFragmentModule module);
}