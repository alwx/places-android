package me.alwx.places.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.utils.EventBus;

/**
 * @author alwx
 * @version 1.0
 */
@Module
public final class EventBusModule {
    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new EventBus();
    }
}
