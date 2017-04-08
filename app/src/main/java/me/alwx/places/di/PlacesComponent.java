package me.alwx.places.di;

import dagger.Subcomponent;
import me.alwx.places.di.modules.LocationModule;
import me.alwx.places.di.modules.PlacesModule;
import me.alwx.places.di.scopes.DataScope;
import me.alwx.places.ui.places.PlacesActivityComponent;
import me.alwx.places.ui.places.PlacesActivityModule;
import me.alwx.places.ui.places_list.PlacesListFragmentComponent;
import me.alwx.places.ui.places_list.PlacesListFragmentModule;
import me.alwx.places.ui.places_map.PlacesMapFragmentComponent;
import me.alwx.places.ui.places_map.PlacesMapFragmentModule;

@DataScope
@Subcomponent(modules={ PlacesModule.class, LocationModule.class})
public interface PlacesComponent {
    PlacesActivityComponent plus(PlacesActivityModule module);

    PlacesListFragmentComponent plus(PlacesListFragmentModule module);

    PlacesMapFragmentComponent plus(PlacesMapFragmentModule module);
}