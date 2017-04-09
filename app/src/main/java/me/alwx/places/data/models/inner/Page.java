package me.alwx.places.data.models.inner;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.google.auto.value.AutoValue;

/**
 * A representation of a page for {@link me.alwx.places.ui.adapters.PlacesPagerAdapter}
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@AutoValue
public abstract class Page {
    public static Page create(int i, Fragment fragment, @IdRes int id, @StringRes int title) {
        return new AutoValue_Page(i, fragment, id, title);
    }

    public abstract int index();

    public abstract Fragment fragment();

    public abstract int id();

    public abstract int title();
}