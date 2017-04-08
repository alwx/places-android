package me.alwx.places.ui.places_list;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import dagger.Module;
import dagger.Provides;
import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.di.scopes.FragmentScope;

@Module
public class PlacesListFragmentModule {
    private PlacesListFragment fragment;

    public PlacesListFragmentModule(PlacesListFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    PlacesListFragmentPresenter providePresenter(PlacesRepository placesRepository) {
        return new PlacesListFragmentPresenter(fragment, placesRepository);
    }

    @Provides
    @FragmentScope
    PlacesAdapter providePlacesAdapter() {
        return new PlacesAdapter(fragment);
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