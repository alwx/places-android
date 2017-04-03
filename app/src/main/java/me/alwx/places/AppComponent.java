package me.alwx.places;

import dagger.Component;
import me.alwx.places.data.components.NetworkComponent;
import me.alwx.places.data.modules.ApiDefaultModule;
import me.alwx.places.data.modules.ApiGoogleModule;
import me.alwx.places.data.PlacesModule;
import me.alwx.places.ui.activities.PlacesActivity;
import me.alwx.places.data.PlacesComponent;

/**
 * @author alwx
 * @version 1.0
 */
@AppScope
@Component(
        dependencies = {NetworkComponent.class},
        modules = {AppModule.class, ApiDefaultModule.class, ApiGoogleModule.class}
)
public interface AppComponent {
    PlacesComponent plus(PlacesModule placesModule);
}
