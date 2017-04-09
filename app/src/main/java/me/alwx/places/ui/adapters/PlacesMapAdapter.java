package me.alwx.places.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v4.view.PagerAdapter;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.alwx.places.R;
import me.alwx.places.data.models.Geodata;
import me.alwx.places.data.models.Place;
import me.alwx.places.databinding.ItemMapPlaceBinding;
import me.alwx.places.ui.fragments.PlacesMapFragment;

/**
 * @author alwx
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

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }

    public void setGeodataList(List<Geodata> geodataList) {
        geodataArray = new LongSparseArray<>();
        for (Geodata geodata : geodataList) {
            geodataArray.append(geodata.id(), geodata);
        }
        notifyDataSetChanged();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

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

        double d = Geodata.calculateDistance(
                geodataArray.get(place.id()),
                location
        );
        if (d == -1d) {
            binding.distance.setText("");
        } else if (d >= 1000) {
            binding.distance.setText(fragment.getString(R.string.kilometers, d / 1000d));
        } else {
            binding.distance.setText(fragment.getString(R.string.meters, (int) d));
        }

        container.addView(v);
        return v;
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
