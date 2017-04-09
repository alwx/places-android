package me.alwx.places.di.modules;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.utils.PermissionsUtils;

/**
 * @author alwx (https://alwx.me)
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
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }

    @Provides
    @Singleton
    PermissionsUtils providePermissionsUtils(Context context) {
        return new PermissionsUtils(context);
    }
}
