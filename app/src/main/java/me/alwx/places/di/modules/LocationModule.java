package me.alwx.places.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.di.scopes.DataScope;
import me.alwx.places.utils.EventBus;
import me.alwx.places.utils.PermissionsRequester;

/**
 * @author alwx
 * @version 1.0
 */

@Module
public final class LocationModule {
    @DataScope
    @Provides
    PermissionsRequester providePermissionsRequester(Context context,
                                                     EventBus eventBus) {
        return new PermissionsRequester(context, eventBus);
    }
}
