package me.alwx.places.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Parent fragment for all fragments.
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDependencyInjector();
    }

    protected abstract void initDependencyInjector();
}
