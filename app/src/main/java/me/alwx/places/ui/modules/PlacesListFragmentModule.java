package me.alwx.places.ui.modules;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.di.scopes.FragmentScope;
import me.alwx.places.ui.adapters.PlacesListAdapter;
import me.alwx.places.ui.fragments.PlacesListFragment;
import me.alwx.places.ui.presenters.PlacesListFragmentPresenter;
import me.alwx.places.utils.LocationUtils;
import me.alwx.places.utils.PageInteractor;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@Module
public class PlacesListFragmentModule {
    private PlacesListFragment fragment;

    public PlacesListFragmentModule(PlacesListFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    PlacesListFragmentPresenter providePresenter(PlacesRepository placesRepository,
                                                 LocationUtils locationUtils,
                                                 PageInteractor interactor) {
        return new PlacesListFragmentPresenter.Builder()
                .setFragment(fragment)
                .setPlacesRepository(placesRepository)
                .setLocationUtils(locationUtils)
                .setPageInteractor(interactor)
                .build();
    }

    @Provides
    @FragmentScope
    PlacesListAdapter providePlacesAdapter() {
        return new PlacesListAdapter(fragment);
    }

    @Provides
    @FragmentScope
    DividerItemDecoration provideItemDecoration() {
        return new DividerItemDecoration(fragment.getActivity(), DividerItemDecoration.VERTICAL);
    }

    @Provides
    @FragmentScope
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    @FragmentScope
    OnRefreshListener provideOnRefreshListener(final PlacesListFragmentPresenter presenter) {
        return new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadPlaces(true);
            }
        };
    }
}