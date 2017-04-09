package me.alwx.places.data.models.inner;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Page {
    public static Page create(Fragment fragment, @IdRes int id, @StringRes int title) {
        return new AutoValue_Page(fragment, id, title);
    }

    public abstract Fragment fragment();

    public abstract int id();

    public abstract int title();
}