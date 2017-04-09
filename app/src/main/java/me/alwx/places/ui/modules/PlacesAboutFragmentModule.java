package me.alwx.places.ui.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.fragments.PlacesAboutFragment;
import me.alwx.places.ui.presenters.PlacesAboutFragmentPresenter;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@Module
public class PlacesAboutFragmentModule {
    private PlacesAboutFragment fragment;

    public PlacesAboutFragmentModule(PlacesAboutFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    PlacesAboutFragmentPresenter providePresenter(Context appContext) {
        return new PlacesAboutFragmentPresenter(fragment, appContext);
    }
}