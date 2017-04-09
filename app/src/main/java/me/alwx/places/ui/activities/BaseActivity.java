package me.alwx.places.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Parent activity for all activities.
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDependencyInjector();
    }

    protected abstract void initDependencyInjector();
}
