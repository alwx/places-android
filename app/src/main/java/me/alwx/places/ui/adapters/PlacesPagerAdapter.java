package me.alwx.places.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Arrays;
import java.util.List;

import me.alwx.places.data.models.inner.Page;

/**
 * Adapter for main ViewPager inside {@link me.alwx.places.ui.activities.PlacesActivity}
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesPagerAdapter extends FragmentStatePagerAdapter {
    private List<Page> pageList;

    public PlacesPagerAdapter(FragmentManager fm, Page... pages) {
        super(fm);
        pageList = Arrays.asList(pages);
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        return getPage(position).fragment();
    }

    @NonNull
    public Page getPage(int position) {
        if (position < pageList.size()) {
            return pageList.get(position);
        }
        return pageList.get(0);
    }

    @Override
    public int getCount() {
        return pageList.size();
    }
}
