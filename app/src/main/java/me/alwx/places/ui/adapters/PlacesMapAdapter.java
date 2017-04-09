package me.alwx.places.ui.adapters;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.alwx.places.R;
import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.databinding.ItemMapPlaceBinding;
import me.alwx.places.ui.fragments.PlacesMapFragment;

/**
 * ViewPager adapter for {@link PlacesMapFragment}
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesMapAdapter extends PagerAdapter {
    private PlacesMapFragment fragment;
    private List<Place> placeList = new ArrayList<>();
    private LongSparseArray<Geodata> geodataArray = new LongSparseArray<>();

    private Location location;

    public PlacesMapAdapter(PlacesMapFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Updates the places list.
     *
     * @param placeList list of {@link Place} objects
     */
    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }

    /**
     * Updates the geodata list.
     *
     * @param geodataList list of {@link Geodata} objects
     */
    public void setGeodataList(List<Geodata> geodataList) {
        geodataArray = new LongSparseArray<>();
        for (Geodata geodata : geodataList) {
            geodataArray.append(geodata.id(), geodata);
        }
        notifyDataSetChanged();
    }

    /**
     * Updates location. We need location to calculate the distance between a place and a user.
     *
     * @param location {@link Location} object
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns a position (index) of an item by its ID.
     *
     * @param id id of an item
     * @return item's index
     */
    public int getPosition(long id) {
        for (int i = 0; i < placeList.size(); i++) {
            if (placeList.get(i).id() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater
                .from(container.getContext())
                .inflate(R.layout.item_map_place, container, false);

        Place place = placeList.get(position);
        ItemMapPlaceBinding binding = DataBindingUtil.bind(v);

        binding.name.setText(place.title());
        binding.address.setText(place.address().asString());

        bindDistance(
                binding.distance,
                Geodata.calculateDistance(
                        geodataArray.get(place.id()),
                        location
                )
        );

        container.addView(v);
        return v;
    }

    /**
     * Displays the distance.
     *
     * @param distanceView TextView for distance information
     * @param distance distance in meters
     */
    private void bindDistance(@NonNull TextView distanceView, double distance) {
        if (distance == -1d) {
            distanceView.setText("");
        } else if (distance >= 1000) {
            distanceView.setText(fragment.getString(R.string.kilometers, distance / 1000d));
        } else {
            distanceView.setText(fragment.getString(R.string.meters, (int) distance));
        }
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
