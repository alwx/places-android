package me.alwx.places.ui.places;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.alwx.places.ui.BaseFragment;
import me.alwx.places.ui.places_about.PlacesAboutFragment;
import me.alwx.places.ui.places_list.PlacesListFragment;
import me.alwx.places.ui.places_map.PlacesMapFragment;

/**
 * @author alwx
 * @version 1.0
 */

class PagerAdapter extends FragmentStatePagerAdapter {
    static final int PAGES_COUNT = 3;

    PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
            case 0:
                return PlacesListFragment.newInstance();
            case 1:
                return PlacesMapFragment.newInstance();
            case 2:
                return PlacesAboutFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }
}
