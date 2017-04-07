package me.alwx.places.ui;

import android.app.Fragment;
import android.os.Bundle;

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
