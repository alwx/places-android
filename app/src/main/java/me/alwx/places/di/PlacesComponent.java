package me.alwx.places.di;

import dagger.Subcomponent;
import me.alwx.places.di.modules.LocationModule;
import me.alwx.places.di.modules.PlacesModule;
import me.alwx.places.di.scopes.DataScope;
import me.alwx.places.ui.components.PlacesActivityComponent;
import me.alwx.places.ui.modules.PlacesActivityModule;
import me.alwx.places.ui.components.PlacesListFragmentComponent;
import me.alwx.places.ui.modules.PlacesListFragmentModule;
import me.alwx.places.ui.components.PlacesMapFragmentComponent;
import me.alwx.places.ui.modules.PlacesMapFragmentModule;

@DataScope
@Subcomponent(modules={ PlacesModule.class, LocationModule.class})
public interface PlacesComponent {
    PlacesActivityComponent plus(PlacesActivityModule module);

    PlacesListFragmentComponent plus(PlacesListFragmentModule module);

    PlacesMapFragmentComponent plus(PlacesMapFragmentModule module);
}