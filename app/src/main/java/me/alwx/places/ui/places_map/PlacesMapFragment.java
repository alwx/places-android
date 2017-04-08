package me.alwx.places.ui.places_map;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.OnMapReadyCallback;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.databinding.FragmentMapBinding;
import me.alwx.places.ui.BaseFragment;
import me.alwx.places.ui.Presenter;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesMapFragment extends BaseFragment {
    private FragmentMapBinding binding;

    @Inject
    PlacesMapFragmentPresenter presenter;

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

    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void initializeDependencyInjector() {
        App.get(getActivity())
                .getPlacesComponent()
                .plus(new PlacesMapFragmentModule(this))
                .inject(this);
    }

    public void initMap(final Bundle state, OnMapReadyCallback callback) {
        binding.map.onCreate(state);
        binding.map.getMapAsync(callback);
        binding.map.onResume();
    }
}
