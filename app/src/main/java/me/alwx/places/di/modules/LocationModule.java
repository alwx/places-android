package me.alwx.places.di.modules;

import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.di.scopes.DataScope;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PermissionsUtils;

/**
 * @author alwx
 * @version 1.0
 */

@Module
public final class LocationModule {
    @DataScope
    @Provides
    LocationUtils provideLocationUtils(GoogleApiClient apiClient,
                                       PermissionsUtils permissionsUtils) {
        return new LocationUtils(apiClient, permissionsUtils);
    }
}
