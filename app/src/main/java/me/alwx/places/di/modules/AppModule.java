package me.alwx.places.di.modules;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.di.scopes.DataScope;
import me.alwx.places.utils.EventBus;
import me.alwx.places.utils.PermissionsUtils;

/**
 * @author alwx
 * @version 1.0
 */
@Module
public final class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }

    @Provides
    @Singleton
    PermissionsUtils providePermissionsUtils(Context context,
                                             EventBus eventBus) {
        return new PermissionsUtils(context, eventBus);
    }
}
