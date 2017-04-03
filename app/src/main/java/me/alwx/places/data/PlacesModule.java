package me.alwx.places.data;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.api.PlacesManager;
import me.alwx.places.data.modules.ApiDefaultModule;

@Module
public class PlacesModule {
    public PlacesModule() {

    }

    @Provides
    @DataScope
    PlacesManager providePlacesManager(ApiDefaultModule.ApiInterface api) {
        return new PlacesManager(api);
    }
}