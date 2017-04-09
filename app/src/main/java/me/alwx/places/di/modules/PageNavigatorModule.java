package me.alwx.places.di.modules;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.di.scopes.DataScope;
import me.alwx.places.utils.PageInteractor;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@Module
public class PageNavigatorModule {
    @DataScope
    @Provides
    PageInteractor providePageNavigator() {
        return new PageInteractor();
    }
}
