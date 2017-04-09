package me.alwx.places.ui.fragments;

import android.content.pm.PackageInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import me.alwx.places.App;
import me.alwx.places.R;
import me.alwx.places.databinding.FragmentAboutBinding;
import me.alwx.places.ui.modules.PlacesAboutFragmentModule;
import me.alwx.places.ui.presenters.PlacesAboutFragmentPresenter;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesAboutFragment extends BaseFragment {
    private FragmentAboutBinding binding;

    @Inject
    PlacesAboutFragmentPresenter presenter;

    public static PlacesAboutFragment newInstance() {
        return new PlacesAboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        binding = DataBindingUtil.bind(view);

        presenter.initializeViews();
        return view;
    }

    @Override
    protected void initializeDependencyInjector() {
        App.get(getActivity())
                .getPlacesComponent()
                .plus(new PlacesAboutFragmentModule(this))
                .inject(this);
    }

    public void setVersionText(String text) {
        binding.appVersion.setText(text);
    }
}
