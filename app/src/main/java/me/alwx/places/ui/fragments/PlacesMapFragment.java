package me.alwx.places.ui.fragments;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.databinding.FragmentMapBinding;
import me.alwx.places.ui.adapters.PlacesMapAdapter;
import me.alwx.places.ui.modules.PlacesMapFragmentModule;
import me.alwx.places.ui.presenters.PlacesActivityPresenter;
import me.alwx.places.ui.presenters.PlacesMapFragmentPresenter;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesMapFragment extends BaseFragment {
    private FragmentMapBinding binding;

    @Inject
    PlacesMapFragmentPresenter presenter;

    @Inject
    PlacesMapAdapter adapter;

    public static PlacesMapFragment newInstance() {
        return new PlacesMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
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

    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void initDependencyInjector() {
        App.get(getActivity())
                .getPlacesComponent()
                .plus(new PlacesMapFragmentModule(this))
                .inject(this);
    }

    /**
     * Initializes map and set all required callbacks.
     * To be called by {@link PlacesMapFragmentPresenter}
     *
     * @param state    application state
     * @param callback {@link OnMapReadyCallback} instance
     */
    public void initMap(final Bundle state, OnMapReadyCallback callback) {
        binding.map.onCreate(state);
        binding.map.getMapAsync(callback);
        binding.map.onResume();
    }

    /**
     * Initializes ViewPager.
     * To be called by {@link PlacesMapFragmentPresenter}
     *
     * @param placeList list of {@link Place} objects
     */
    public void initPager(List<Place> placeList) {
        binding.pager.setAdapter(adapter);
        adapter.setPlaceList(placeList);
    }

    /**
     * Sets {@link ViewPager.OnPageChangeListener} listener for view pager.
     * To be called by {@link PlacesMapFragmentPresenter}
     *
     * @param listener on page change listener
     */
    public void setPagerCallbacks(ViewPager.OnPageChangeListener listener) {
        binding.pager.addOnPageChangeListener(listener);
    }

    /**
     * Removes the callback.
     * To be called by {@link PlacesMapFragmentPresenter}
     *
     * @param listener on page change listener
     */
    public void clearPagerCallbacks(ViewPager.OnPageChangeListener listener) {
        binding.pager.removeOnPageChangeListener(listener);
    }

    /**
     * Provides a list of Geodata objects for adapter.
     * To be called by {@link PlacesMapFragmentPresenter}
     *
     * @param geodataList list of {@link Geodata} objects
     */
    public void setAdapterGeodataList(List<Geodata> geodataList) {
        adapter.setGeodataList(geodataList);
    }

    /**
     * Provides a Location for adapter.
     * To be called by {@link PlacesMapFragmentPresenter}
     *
     * @param location {@link Location} object
     */
    public void setAdapterLocation(Location location) {
        adapter.setLocation(location);
    }

    /**
     * Navigates to an item with specified id.
     * To be called by {@link PlacesMapFragmentPresenter}
     *
     * @param id item id
     */
    public void pagerNavigateTo(long id) {
        int position = adapter.getPosition(id);
        if (position != -1) {
            binding.pager.setCurrentItem(position);
        }
    }
}
