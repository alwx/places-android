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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.data.models.Place;
import me.alwx.places.ui.fragments.modules.PlacesListFragmentModule;
import me.alwx.places.databinding.FragmentPlacesBinding;
import me.alwx.places.ui.adapters.PlacesAdapter;
import me.alwx.places.ui.fragments.presenters.PlacesListFragmentPresenter;

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

    @Override
    protected void setupFragmentComponent() {
        App.get(getActivity())
                .getPlacesComponent()
                .plus(new PlacesListFragmentModule(this))
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        binding = DataBindingUtil.bind(view);

        initList();
        presenter.loadPlaces();

        return view;
    }

    public void showLoading(boolean isLoading) {
        binding.list.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        binding.empty.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        binding.loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    public void setPlaceList(List<Place> placeList) {
        adapter.setPlaceList(placeList);
    }

    protected void initList() {
        if (adapter.getItemCount() == 0) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.list.setVisibility(View.GONE);
            return;
        }

        binding.empty.setVisibility(View.GONE);
        binding.list.setVisibility(View.VISIBLE);
        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.list.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        binding.list.setAdapter(adapter);
    }
}
