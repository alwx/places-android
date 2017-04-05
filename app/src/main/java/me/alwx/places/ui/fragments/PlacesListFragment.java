package me.alwx.places.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.data.models.Place;
import me.alwx.places.ui.fragments.modules.PlacesListFragmentModule;
import me.alwx.places.databinding.FragmentPlacesBinding;
import me.alwx.places.ui.adapters.PlacesAdapter;
import me.alwx.places.ui.presenters.PlacesListFragmentPresenter;
import timber.log.Timber;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesListFragment extends BaseFragment {
    private FragmentPlacesBinding binding;

    @Inject
    PlacesAdapter adapter;

    @Inject
    PlacesListFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        binding = DataBindingUtil.bind(view);

        initList();
        presenter.getPlaces();

        return view;
    }

    @Override
    protected void initializeDependencyInjector() {
        App.get(getActivity())
                .getPlacesComponent()
                .plus(new PlacesListFragmentModule(this))
                .inject(this);
    }

    protected void initList() {
        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.list.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        binding.list.setAdapter(adapter);
    }

    public void showLoading(boolean isLoading) {
        binding.list.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        //binding.empty.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        binding.loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    public void setPlaceList(List<Place> placeList) {
        adapter.setPlaceList(placeList);
        adapter.notifyDataSetChanged();
    }
}
