package me.alwx.places.ui.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.alwx.places.R;
import me.alwx.places.databinding.FragmentAboutBinding;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesAboutFragment extends Fragment {
    private FragmentAboutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        binding = DataBindingUtil.bind(view);

        return view;
    }
}
