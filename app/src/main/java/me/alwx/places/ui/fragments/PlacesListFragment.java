package me.alwx.places.ui.fragments;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.databinding.FragmentPlacesBinding;
import me.alwx.places.ui.adapters.PlacesListAdapter;
import me.alwx.places.ui.modules.PlacesListFragmentModule;
import me.alwx.places.ui.presenters.PlacesListFragmentPresenter;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesListFragment extends BaseFragment {
    private FragmentPlacesBinding binding;

    @Inject
    PlacesListFragmentPresenter presenter;

    @Inject
    PlacesListAdapter adapter;

    @Inject
    DividerItemDecoration dividerItemDecoration;

    @Inject
    RecyclerView.LayoutManager layoutManager;

    @Inject
    SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public static PlacesListFragment newInstance() {
        return new PlacesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void initDependencyInjector() {
        App.get(getActivity())
                .getPlacesComponent()
                .plus(new PlacesListFragmentModule(this))
                .inject(this);
    }

    /**
     * Initializes places list and sets all required decorators and layout managers.
     * Also binds onRefreshListener.
     * To be called by {@link PlacesListFragmentPresenter}
     */
    public void initPlaceList() {
        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(layoutManager);
        binding.list.addItemDecoration(dividerItemDecoration);
        binding.list.setAdapter(adapter);
        binding.refresh.setOnRefreshListener(onRefreshListener);
    }

    /**
     * Changes the loading state.
     * To be called by {@link PlacesListFragmentPresenter}
     *
     * @param loading boolean indicator
     */
    public void changeLoadingState(boolean loading) {
        binding.refresh.setRefreshing(loading);
    }

    /**
     * Displays the error.
     * To be called by {@link PlacesListFragmentPresenter}
     */
    public void showError() {
        binding.list.setVisibility(View.GONE);
        binding.empty.setVisibility(View.VISIBLE);
    }

    /**
     * Provides a list of places for adapter.
     * To be called by {@link PlacesListFragmentPresenter}
     *
     * @param placeList list of {@link Place} objects
     */
    public void showPlaceList(List<Place> placeList) {
        binding.list.setVisibility(View.VISIBLE);
        binding.empty.setVisibility(View.GONE);

        adapter.setPlaceList(placeList);
    }

    /**
     * Provides a list of Geodata objects for adapter.
     * To be called by {@link PlacesListFragmentPresenter}
     *
     * @param geodataList list of {@link Geodata} objects
     */
    public void setAdapterGeodataList(List<Geodata> geodataList) {
        adapter.setGeodataList(geodataList);
    }

    /**
     * Provides a Location for adapter.
     * To be called by {@link PlacesListFragmentPresenter}
     *
     * @param location {@link Location} object
     */
    public void setAdapterLocation(Location location) {
        adapter.setLocation(location);
    }

    /**
     * Sets a OnClickListener for adapter.
     * To be called by {@link PlacesListFragmentPresenter}
     *
     * @param listener instance of OnClickListener
     */
    public void setAdapterOnClickListener(PlacesListAdapter.OnClickListener listener) {
        adapter.setOnClickListener(listener);
    }
}
