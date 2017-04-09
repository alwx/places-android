package me.alwx.places.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDependencyInjector();
    }

    protected abstract void initializeDependencyInjector();
}
