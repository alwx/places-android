package me.alwx.places.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;

import me.alwx.places.ui.activities.BaseActivity;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFragmentComponent();
    }

    protected abstract void setupFragmentComponent();
}
